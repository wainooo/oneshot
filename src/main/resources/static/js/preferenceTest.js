// 전역 변수로 model 선언
let X_train, X_test, y_train, y_test;
let model;
let dataPoints; // 로드된 데이터를 저장할 변수
let labels = ['과실주', '약주/청주', '탁주']; // 주종 리스트
let isTraining = false;
let isTrainingComplete = false; // 모델 훈련 완료 여부를 추적하는 변수
let modelTrained = false; // 모델 훈련 상태 변수

async function run() {
    const liquor = await tf.data.csv("csv/preference_results.csv", {
        columnNames: ["sweetTaste","sourTaste","refreshing","bodyFeeling","mainLiquor"],
        columnConfigs: {
            sweetTaste: { isLabel : false},
            sourTaste: { isLabel : false},
            refreshing: { isLabel : false},
            bodyFeeling: { isLabel : false},
            mainLiquor: {isLabel : true}
        },
        configuredColumnsOnly: true
    });

    console.log(await tf.data.csv("csv/preference_results.csv").take(10).toArray()) //데이터 URL출력

    // 데이터를 배열로 변환하고 출력
    dataPoints = await liquor.toArray();

    // 데이터 매핑
    dataPoints = dataPoints.map(({xs, ys}) => ({
        x: [
            parseFloat(xs.sweetTaste),
            parseFloat(xs.sourTaste),
            parseFloat(xs.refreshing),
            parseFloat(xs.bodyFeeling)
        ], //배열로 묶기, 문자열을 숫자로 변환
        y: labels.indexOf(ys.mainLiquor) // 주종 이름을 숫자로 변환
    }));

    //데이터 셔플 후 출력
    tf.util.shuffle(dataPoints)

    //데이터 매핑(변환) 후 결과 출력
    console.log(dataPoints)

    // 히트맵 그리기
    drawHeatmaps(dataPoints, labels);
}
run()

function drawHeatmaps(dataPoints, labels) {
    const features = ['단맛', '신맛', '청량감', '바디감'];
    const tasteLevels = [1, 2, 3, 4, 5];

    features.forEach((feature, featureIndex) => {
        const heatmapData = [];

        labels.forEach((label, labelIndex) => {
            const row = [];
            tasteLevels.forEach((level) => {
                const filteredData = dataPoints.filter(dp => dp.y === labelIndex && dp.x[featureIndex] === level);
                row.push(filteredData.length);
            });
            heatmapData.push(row);
        });

        const containerId = `heatmapPlot${featureIndex + 1}`;
        const container = document.getElementById(containerId);
        container.innerHTML = '';

        const baseColors = {
            '단맛': '#f8c6d8',
            '신맛': '#f9e580',
            '청량감': '#b3e5fc',
            '바디감': '#d7b899'
        };

        const maxCount = Math.max(...heatmapData.flat());

        const heatmapContainer = document.createElement('div');
        heatmapContainer.style.display = 'flex';
        heatmapContainer.style.flexDirection = 'column';
        heatmapContainer.style.alignItems = 'center';
        container.appendChild(heatmapContainer);

        const xAxisTitle = document.createElement('div');
        xAxisTitle.innerText = '맛의 정도 (1부터 5까지)';
        xAxisTitle.style.textAlign = 'right';
        xAxisTitle.style.marginBottom = '10px';
        xAxisTitle.style.marginLeft = '30px'; // 오른쪽으로 한 칸 이동
        heatmapContainer.appendChild(xAxisTitle);

        const rowsContainer = document.createElement('div');
        heatmapContainer.appendChild(rowsContainer);

        // 테이블에 주종 이름을 추가
        heatmapData.forEach((row, rowIndex) => {
            const rowContainer = document.createElement('div');
            rowContainer.style.display = 'flex';
            rowContainer.style.alignItems = 'center';

            // 주종 이름을 각 행의 앞쪽에 추가
            const yLabel = document.createElement('div');
            yLabel.innerText = labels[rowIndex];
            yLabel.style.width = '75px'; // 주종 이름 너비
            yLabel.style.textAlign = 'right'; // 오른쪽 정렬
            yLabel.style.marginRight = '10px';
            rowContainer.appendChild(yLabel);

            // 각 셀 추가
            row.forEach((count) => {
                const cell = document.createElement('div');
                cell.style.width = '55px';
                cell.style.height = '55px';
                cell.style.display = 'flex';
                cell.style.justifyContent = 'center';
                cell.style.alignItems = 'center';
                cell.style.color = '#777';
                cell.innerText = count;

                const colorScale = d3.scaleLinear()
                    .domain([0, maxCount])
                    .range([baseColors[feature] + '33', baseColors[feature]]);
                cell.style.backgroundColor = colorScale(count);

                rowContainer.appendChild(cell);
            });

            rowsContainer.appendChild(rowContainer);
        });

        // x축 레이블 추가
        const xAxisContainer = document.createElement('div');
        xAxisContainer.style.display = 'flex';
        xAxisContainer.style.justifyContent = 'center'; // 중앙 정렬
        xAxisContainer.style.marginTop = '5px';
        xAxisContainer.style.width = '100%'; // 너비를 100%로 설정
        xAxisContainer.style.marginLeft = '78px';
        heatmapContainer.appendChild(xAxisContainer);

        tasteLevels.forEach(level => {
            const xLabel = document.createElement('span');
            xLabel.innerText = level;
            xLabel.style.display = 'inline-block';
            xLabel.style.width = '55px'; // 각 레이블의 너비를 셀과 동일하게 설정
            xLabel.style.textAlign = 'center'; // 중앙 정렬
            xAxisContainer.appendChild(xLabel);
        });

        // xAxisContainer의 너비를 맞추어 셀과 일치하도록 설정
        xAxisContainer.style.width = `${tasteLevels.length * 55}px`; // 전체 너비 설정

        // 그래프 제목을 중앙에 위치시키기 위한 요소
        const titleElement = document.createElement('h4');
        titleElement.className = 'text-center';
        titleElement.innerText = feature;
        titleElement.style.marginTop = '10px';
        titleElement.style.marginLeft = '50px'; // 오른쪽으로 한 칸 이동
        titleElement.innerText = `${feature} 히트맵`; // 제목 형식 수정
        heatmapContainer.appendChild(titleElement); // 제목을 히트맵 아래에 추가
    });


    document.getElementById("train-button").removeAttribute("disabled");
    run1(dataPoints);
}

async function run1(dataPoints) {

    document.getElementById('hr').innerHTML = "<hr>";

    // feature (X)와 label (y)을 분리합니다.
    const featureValues = dataPoints.map(v => v.x); // feature
    const labelValues = dataPoints.map(v => v.y);   // label

    // feature 텐서를 생성합니다.
    const featureTensor = tf.tensor2d(featureValues, [featureValues.length, 4]);
    featureTensor.print(true);  // 생성된 feature 텐서 확인

    // label 텐서를 생성합니다.
    let labelTensor = tf.tensor1d(labelValues, "int32");
    labelTensor.print(true);  // 생성된 label 텐서 확인

    // label을 one-hot encoding합니다.
    labelTensor = tf.oneHot(labelTensor, 3);
    labelTensor.print(true);  // one-hot encoded label 확인

    // 데이터 크기를 확인하여 문제가 없는지 확인합니다.
    console.log(`Feature Tensor Shape: ${featureTensor.shape}`);
    console.log(`Label Tensor Shape: ${labelTensor.shape}`);

    // train/test split
    const trainLen = Math.floor(featureTensor.shape[0] * 0.75);  // 75% 데이터는 학습용
    const testLen = featureTensor.shape[0] - trainLen;           // 나머지는 테스트용

    // 데이터를 나눕니다.
    if (trainLen > 0 && testLen > 0) {  // 크기가 0 이상인지 확인
        [X_train, X_test] = tf.split(featureTensor, [trainLen, testLen]);
        [y_train, y_test] = tf.split(labelTensor, [trainLen, testLen]);
    } else {
        console.error("데이터를 나누기 위한 적절한 크기가 아닙니다.");
        return;
    }

    // 분리된 텐서를 출력하여 확인합니다.
    // console.log("Train Features:");
    X_train.print(true);
    // console.log("Test Features:");
    X_test.print(true);
    // console.log("Train Labels:");
    y_train.print(true);
    // console.log("Test Labels:");
    y_test.print(true);

    document.getElementById("train-button").removeAttribute("disabled");
}

//모델 훈련하기
async function train() {
    // 모델 훈련 로직 수행 중
    document.getElementById('model-status').innerText = '모델이 훈련 중입니다. ing ...';
    isTraining = true;
    document.getElementById("train-button").disabled = true; // 훈련 버튼 비활성화
    document.getElementById("submit-button").disabled = true; // 제출 버튼 비활성화
    document.getElementById("predict-button").disabled = true; // 예측 버튼 비활성화

    // 훈련 완료 후 슬라이더 활성
    // sequential 모델 생성 및 설정
    model = tf.sequential();

    //1-layer
    model.add(
        tf.layers.dense({
            units: 128, //유닛 128에서 256로 수정 -> 입력 처리 강화
            inputShape: [4],
            activation: "relu"
        }) //4*100+100=500
    );
    // model.add(tf.layers.dropout({ rate: 0.5 })); // 드롭아웃 추가

    //2-layer
    model.add
    (tf.layers.dense({
            units: 64,
            activation: "relu" //100*50+50=5050
        })
    )
    // model.add(tf.layers.dropout({ rate: 0.5 })); // 드롭아웃 추가

    //3-layer
    model.add(
        tf.layers.dense({
            units: 32,
            activation: "relu" //50*3+3=153
        })
    );

    // 출력층 (3개의 클래스에 대해 softmax 적용)
    model.add(
        tf.layers.dense({
            units: 3,
            activation: "softmax"
        })
    );

    // Adam 옵티마이저의 학습률을 낮춰 모델이 더 천천히 학습하게 함
    model.compile({
        loss: "categoricalCrossentropy", //손실함수
        optimizer: tf.train.adam(0.01), //최적화 알고리즘(학습률 0.001로 조정)
        metrics: ["accuracy"] //평가지표, accuracy는 정확도, 예측이 맞는 비율을 평가
    });

    //SUMMARY 모델 구조 시각화
    const surface3 = document.querySelector("#summary");
    tfvis.show.modelSummary(surface3, model);

    // Early stopping을 위한 콜백 함수 설정 -> 과적합 방지하면서 시간 절약됨
    const earlyStopping = tf.callbacks.earlyStopping({monitor: 'val_loss', patience: 3});

    //모델 훈련(fit)
    const surface4 = document.querySelector("#fitCallback");
    const history = await model.fit(X_train, y_train, {
        epochs: 32, //에포크 수를 늘려 더 많은 학습 기회를 제공
        batchSize: 64, //배치 크기를 늘려 훈련 시간을 단축(32에서 64로 증가)
        validationData: [X_test, y_test],
        callbacks: [tfvis.show.fitCallbacks(surface4, [
            "loss", //train 손실
            "acc", //train 정확도
            "val_loss", //test 손실
            "val_acc" //test 정확도
        ])]
    });

    // 훈련 완료 처리
    setTimeout(() => {
        const modelStatus = document.getElementById("model-status");
        modelStatus.textContent = "모델 훈련이 완료되었습니다!!!";
        modelStatus.style.fontSize = "20px";  // 폰트 크기 크게
        modelStatus.style.fontWeight = "bold";  // 폰트 굵게
        isTrainingComplete = true; // 훈련이 끝났음을 표시
        enableSliders(); //  훈련 완료 후 슬라이더 활성화
        // 훈련 완료 후 버튼 활성화 체크
        document.getElementById("predict-button").disabled = false; // 예측 버튼 활성화
        // document.getElementById("submit-button").disabled = false; // 예측 버튼 활성화
    }, ); // 모델 훈련이 완료되었다고 가정하고 2초 후에 예측 버튼 활성화


    // 혼동 행렬 및 클래스 정확도 시각화
    //confusion maxtrix 시각화
    const pred = model.predict(X_test);
    pred.print() //예측
    y_test.print() //정답

    //비교
    pred.argMax(1).print()
    y_test.argMax(1).print()

    //행(axis = 0) ↓ 또는 열(axis = 1) ->을 따라 가장 큰 값의 색인을 찾는다. 기본적으로 가장 큰 값의 인덱스는 배열을 평면화하여 찾는다.
    //반환 : 전체 배열에서 가장 높은 값을 가진 요소의 인덱스 반환
    const trueValue = tf.tidy(() => y_test.argMax(1));
    const predValue = tf.tidy(() => pred.argMax(1));

    const confusionMatrix = await tfvis.metrics.confusionMatrix( // confusion matrix 데이터 얻기 (사용자 정의 함수)
        trueValue,
        predValue
    );

    // Confusion matrix 시각화 (오른쪽에 렌더링)
    document.getElementById('matrixTitle').innerHTML ="<p>confusion matrix 시각화</p><p>테스트 데이터를 이용하여 학습한 후 예측한 정답률을 maxtrix로 표현함</p>"
    let container = document.querySelector("#matrix"); // 오른쪽 matrix 위치
    tfvis.render.confusionMatrix(container, { values: confusionMatrix, tickLabels: labels });

    // Per class accuracy 시각화 (오른쪽에 렌더링)
    //클라스별로 얼마나 정확하게 맞추는지
    const classAccuracy = await tfvis.metrics.perClassAccuracy(
        trueValue,
        predValue
    )
    console.log(classAccuracy) // 정확도 데이터를 콘솔에 출력

    let container1 = document.querySelector("#classAccuracy"); // 오른쪽 perClassAccuracy 위치
    tfvis.show.perClassAccuracy(container1, classAccuracy)

    //memory 정리
    pred.dispose();
    trueValue.dispose();
    predValue.dispose();
}

// 슬라이더의 값을 화면에 업데이트하는 함수
function updateValue(slider, displayId) {
    document.getElementById(displayId).textContent = slider.value;
}

// 슬라이더를 활성화하는 함수
function enableSliders() {
    document.querySelectorAll('input[type="range"]').forEach(slider => {
        slider.disabled = false; // 슬라이더 활성화
        slider.value = 0; // 초기값을 0으로 설정
        slider.addEventListener('input', () => {
            updateValue(slider, `range-value-${slider.dataset.index}`); // slider에 데이터 인덱스가 있을 경우
            checkPredictButtonStatus(); // 슬라이더 값 변경 시 버튼 상태 체크
        });
    });
}
function checkPredictButtonStatus() {
    const sweetValue = parseInt(document.querySelector('.range-sweet').value);
    const sourValue = parseInt(document.querySelector('.range-sour').value);
    const refreshingValue = parseInt(document.querySelector('.range-refreshing').value);
    const bodyFeelingValue = parseInt(document.querySelector('.range-body').value);

    const predictButton = document.getElementById('predict-button');

    // 모든 슬라이더 값이 1 이상이고 모델 훈련이 완료된 경우에만 버튼 활성화
    const allValuesEntered = sweetValue > 0 && sourValue > 0 && refreshingValue > 0 && bodyFeelingValue > 0;
    predictButton.disabled = !(allValuesEntered && isTrainingComplete);
}

// "예측하기" 버튼을 눌렀을 때 실행되는 함수
function predict() {
    const sweetValue = parseFloat(document.getElementById('sweetTaste').value);
    const sourValue = parseFloat(document.getElementById('sourTaste').value);
    const refreshingValue = parseFloat(document.getElementById('refreshing').value);
    const bodyFeelingValue = parseFloat(document.getElementById('bodyFeeling').value);

    // 모든 값이 1이상인 경우에만 예측을 수행합니다.
    const allValuesEntered = sweetValue > 0 && sourValue > 0 && refreshingValue > 0 && bodyFeelingValue > 0;

    if (allValuesEntered && !isNaN(sweetValue) && !isNaN(sourValue) && !isNaN(refreshingValue) && !isNaN(bodyFeelingValue)) {
        const inputTensor = tf.tensor2d([[sweetValue, sourValue, refreshingValue, bodyFeelingValue]], [1, 4]);

        // 모델이 예측을 수행합니다.
        const prediction = model.predict(inputTensor);

        // 예측 값에서 가장 큰 인덱스를 가져옵니다.
        const predictedIndex = prediction.argMax(-1).dataSync()[0];

        // 예측된 인덱스에 해당하는 주종을 가져옵니다.
        const predictedLiquor = labels[predictedIndex];
        document.getElementById('predictedLiquor').value = predictedLiquor; // 숨겨진 필드에 값 저장

        // 결과를 표시합니다.
        document.getElementById('predict-output').innerText = `회원님께서 선호하는 주종은 '${predictedLiquor}'입니다.`;

        // 예측이 완료되면 제출 버튼 활성화
        document.getElementById("submit-button").disabled = false;
    } else {
        document.getElementById('predict-output').innerText = ''; // 값이 없으면 결과를 비웁니다.
    }
}



// 페이지 로드 시 '제출하기' 버튼 비활성화 상태 설정
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("submit-button").disabled = true;
    document.getElementById('predict-button').disabled = true; // 예측 버튼도 초기에는 비활성화
    enableSliders();
});