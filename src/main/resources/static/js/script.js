//img

var i = 0;
var n = null;
var repeat;

timer();

function timer() {
    repeat = setInterval(function() {
        next();
    }, 3000);
}

function slider() {
    // 현재 슬라이드와 이전 슬라이드 설정
    n = (i === 0) ? 2 : i - 1; // n은 항상 이전 슬라이드 인덱스

    $(".slide_cover li").eq(i).css("z-index", "6").stop().animate({
        "width": "100%",
        "opacity": "1"
    }, 2000, function() {
        $(window).trigger("resize");

        $(".slide_cover li").eq(n).css({
            "width": "0",
            "opacity": "0" // 슬라이드 사라짐
        });

        $("ul.slide_cover > li").find(".m_textbox").removeClass("on");
        $("ul.slide_cover > li").eq(i).find(".m_textbox").addClass("on");

        setTimeout(function() {
            timer();
        }, 10);
    });

}

function next() {
    clearInterval(repeat);
    i = (i + 1) % 3; // 0, 1, 2 중 하나로 설정
    slider();
}

function prev() {
    clearInterval(repeat);
    i = (i - 1 + 3) % 3; // 0, 1, 2 중 하나로 설정
    slider();
}


// header
$(document).ready(function(){
    $(window).resize(function(){
        let h = $(window).height();
        $(".start_cover").height(h);
    });

    $(window).trigger("resize");

    setTimeout(function(){

        $(".start_cover").stop().fadeOut(600);

    },3000);
});

// body
$(document).ready(function() {

// Intersection Observer 설정
    const animateElements = document.querySelectorAll('.animate');
    const observerOptions = {
        root: null,
        rootMargin: '0px',
        threshold: 0.3 // 30%가 보일 때 애니메이션 시작
    };

    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('show');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    animateElements.forEach(element => {
        observer.observe(element);
    });
});




// nav 라인
$(function(){

    $(window).scroll(function(){
        let s = $(window).scrollTop();

        let max = $(document).height()-$(window).height();

        let result = (s/max)*100

        $(".line").css("width",result+"%");

        console.log(s,max);
    });

});

document.addEventListener("DOMContentLoaded", function() {
    const navbar = document.querySelector('.navbar');

    window.addEventListener('scroll', function() {
        if (window.scrollY > 50) { // 스크롤 위치가 50px을 넘으면
            navbar.classList.add('scrolled'); // 배경색 변경 클래스 추가
        } else {
            navbar.classList.remove('scrolled'); // 클래스 제거
        }
    });
});

$(function() {
    // 작은 화면에서만 아코디언 기능 추가
    $('.sub-item > a').click(function(e) {
        if ($(window).width() <= 768) { // sm 이하일 때만 적용
            e.preventDefault(); // 링크의 기본 동작 방지
            const target = $(this).next('.collapse'); // 다음 .collapse 요소 선택

            // 현재 메뉴 외의 모든 아코디언 닫기
            $('.sub-item .collapse').not(target).slideUp(200);

            // 클릭한 메뉴의 아코디언 토글
            target.stop(true, true).slideToggle(200);
        }
    });
});

$(document).ready(function () {
    $('.navbar-toggler').click(function () {
        $('.navbar-nav').collapse('toggle');
    });

    $('.navbar-toggler').click(function () {
        $('.navbar-nav').collapse('hide');
    });
});

// se6
$(document).ready(function(){


    $(window).scroll(function(){
        var window_scroll_top = $(window).scrollTop();

        $(".se6_cover").each(function(){
            var window_height = $(window).height()/1.1;
            var img_offset = $(this).offset().top-window_height;

            if(window_scroll_top > img_offset){
                $(this).animate({
                    "transform":"translateY(0)",
                    "opacity":"1"
                },800);
            }
        });
    });

    $(window).trigger("scroll");

});


