// YouTube 비디오 데이터 배열
const videos = [
  {
      id: 1,
      title: "입문용 전통주 싹 정리했습니다! 전통주 추천 가이드",
      thumbnail: "https://img.youtube.com/vi/WeHIAdxKkFc/mqdefault.jpg",
      url: "https://www.youtube.com/embed/WeHIAdxKkFc"
  },
  {
      id: 2,
      title: "한번쯤은 꼭 마셔봐야 할 전통주, 일엽편주 리뷰",
      thumbnail: "https://img.youtube.com/vi/LAbN5k5nGEc/mqdefault.jpg",
      url: "https://www.youtube.com/embed/LAbN5k5nGEc"
  },
  {
    id: 3,
    title: "이 가격에 1위? 이마트 최고 가성비 소주는?",
    thumbnail: "https://img.youtube.com/vi/ZTxDqUOUwns/mqdefault.jpg",
    url: "https://www.youtube.com/embed/ZTxDqUOUwns"
},
{
    id: 4,
    title: "양조장 500곳 다닌 찐덕후가 꼽은 ‘내 인생 최고 전통주 5가지’ (feat.백곰 이승훈) | 주락이월드 / 14F",
    thumbnail: "https://img.youtube.com/vi/xv4QEHErEh4/mqdefault.jpg",
    url: "https://www.youtube.com/embed/xv4QEHErEh4"
},
{
    id: 5,
    title: "전통주 소믈리에가 마셔본 요즘 잘나가는 막걸리, 소주 ㄷㄷ",
    thumbnail: "https://img.youtube.com/vi/3h6XdJ7__sc/mqdefault.jpg",
    url: "https://www.youtube.com/embed/3h6XdJ7__sc"
},
{
    id: 6,
    title: "속지마세요 '진짜 안동소주' 제대로 추천해드립니다!",
    thumbnail: "https://img.youtube.com/vi/j8dkn8_38yA/mqdefault.jpg",
    url: "https://www.youtube.com/embed/j8dkn8_38yA"
},
{
    id: 7,
    title: "갓성비 증류식 소주 딱 추천 드립니다. 영상 찍다가 인생 소주 찾았습니다. (진심)",
    thumbnail: "https://img.youtube.com/vi/xzZsD-4UVoU/mqdefault.jpg",
    url: "https://www.youtube.com/embed/xzZsD-4UVoU"
},
{
    id: 8,
    title: "3.5억짜리 증류기로 만든 소주는 무슨 맛일까? / 맛있는 소주 3가지 추천까지",
    thumbnail: "https://img.youtube.com/vi/K4IsCnqYcWI/mqdefault.jpg",
    url: "https://www.youtube.com/embed/K4IsCnqYcWI"
},
{
    id: 9,
    title: "[ 전통주 ] 이강주 | 전주에서 배, 생강으로 만든 조선 3대 명주. 무형문화재. 식품명인",
    thumbnail: "https://img.youtube.com/vi/pxfvFBdi4s0/mqdefault.jpg",
    url: "https://www.youtube.com/embed/pxfvFBdi4s0"
},
{
    id: 10,
    title: "이 호리병이 겨우 2만원..? 진짜 착한 가격의 선물용 전통주 특집! 🎁",
    thumbnail: "https://img.youtube.com/vi/_BqqrCvJpJM/mqdefault.jpg",
    url: "https://www.youtube.com/embed/_BqqrCvJpJM"
},
{
    id: 11,
    title: "증류식 소주가 이렇게 맛있었어?!🥃 이 정도면 위스키 안 부럽다?! | 주락이월드 / 14F",
    thumbnail: "https://img.youtube.com/vi/AL_bL1jPgDs/mqdefault.jpg",
    url: "https://www.youtube.com/embed/AL_bL1jPgDs"
},
{
    id: 12,
    title: "선물하면 실패 없는 술, 품절되기 전에 보세요!",
    thumbnail: "https://img.youtube.com/vi/BTuptQQgSm4/mqdefault.jpg",
    url: "https://www.youtube.com/embed/BTuptQQgSm4"
},
{
    id: 13,
    title: "막걸리, 안동소주, 이화주, 진도홍주, 문배주 등 전통주 통합편 ‪@세모지‬",
    thumbnail: "https://img.youtube.com/vi/BkNKO5JskC0/mqdefault.jpg",
    url: "https://www.youtube.com/embed/BkNKO5JskC0"
},
{
    id: 14,
    title: "혼자 남은 유부남의 은밀한 취미",
    thumbnail: "https://img.youtube.com/vi/2qLlHXNQjzU/mqdefault.jpg",
    url: "https://www.youtube.com/embed/2qLlHXNQjzU"
},
{
    id: 15,
    title: "[ 전통주 ] 밀담 52 | 해적 잭 스패로우도 좋아할 한국의 술",
    thumbnail: "https://img.youtube.com/vi/dRP_N0jMGdc/mqdefault.jpg",
    url: "https://www.youtube.com/embed/dRP_N0jMGdc"
},
{
    id: 16,
    title: "[설특집] 외국인들의 전통주 체험! 대통대잎술과 한산소곡주를 마신 반응은? 외국인들도 푹 빠져버린 전통주! 리뷰 갑니다 #대잎술 #한산소곡주 #전통주 #eating show",
    thumbnail: "https://img.youtube.com/vi/FrCM1Fk23Gs/mqdefault.jpg",
    url: "https://www.youtube.com/embed/FrCM1Fk23Gs"
},
{
    id: 17,
    title: "말이 안되는 가격이라구요? 맛이 더 말이 안됩니다… | 2024 우리술품평회 (2편)",
    thumbnail: "https://img.youtube.com/vi/a2doPNBWkIU/mqdefault.jpg",
    url: "https://www.youtube.com/embed/a2doPNBWkIU"
},
{
    id: 18,
    title: "반박불가 올해의 대상 받은 막걸리 마셔봄 ㄷㄷ | 2024 우리술품평회",
    thumbnail: "https://img.youtube.com/vi/4rsf6tO5G-k/mqdefault.jpg",
    url: "https://www.youtube.com/embed/4rsf6tO5G-k"
}

  // 여기에 나머지 비디오 데이터 추가
];

// 페이지네이션 설정
const videosPerPage = 9;
let currentPage = 1;

// 페이지 로드 시 비디오 로드
$(document).ready(() => {
  loadVideos();
  $('#nextBtn').click(() => {
      currentPage++;
      loadVideos();
  });
  $('#prevBtn').click(() => {
      currentPage--;
      loadVideos();
  });
});

// 비디오 로드 함수
function loadVideos() {
    const startIndex = (currentPage - 1) * videosPerPage;
    const endIndex = Math.min(startIndex + videosPerPage, videos.length);
    const videoContainer = $('#videoContainer');

    videoContainer.empty();

    for (let i = startIndex; i < endIndex; i++) {
        const video = videos[i];
        const videoCard = `
          <div class="m-box col-lg-4 col-md-6 mb-4 mx-auto">
              <a href="/youtube/detail?id=${video.id}">
                <div class="px-2">
                    <img src="${video.thumbnail}" class="img-fluid rounded" width="500px" height="300px" alt="${video.title}">
                </div>
              </a>
              <div class="text-center mt-3 mb-5">
                  <a href="/youtube/detail?id=${video.id}" class="link-dark text-decoration-none">${video.title}</a>
              </div>
          </div>
      `;
        videoContainer.append(videoCard);
    }

    $('#prevBtn').prop('disabled', currentPage === 1);
    $('#nextBtn').prop('disabled', endIndex >= videos.length);
}

// 상세 페이지 로드
$(document).ready(() => {
  const urlParams = new URLSearchParams(window.location.search);
  const videoId = urlParams.get('id');

  if (videoId) {
      const video = videos.find(v => v.id == videoId);
      if (video) {
          $('#videoDetail').html(`
            <div class="col">
                 <h1 class="mt-5 mb-5 text-center">${video.title}</h1>
                <div style="position: relative;width:100%;padding-bottom: 56.25%;">
                    <iframe src="${video.url}" style=" position: absolute;width: 100%;height: 100%;" title="YouTube video player" frameborder="0" ></iframe>
                </div>
            </div>
          `);
      }
  }
});
