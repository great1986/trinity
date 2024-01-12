window.onload = function(){
    // 게시글 목록 페이지 내 요소 (검색)
    const searchFrm = document.querySelector("#searchForm");
    const searchBtn = document.querySelector(".btn-search");
    const clearBtn = document.querySelector(".btn-clear");

    // 검색 버튼(search) 클릭 이벤트
    searchBtn.addEventListener("click", ()=>{
        searchFrm.submit();
    });

    // 검색 버튼(clear) 클릭 이벤트
    clearBtn.addEventListener("click", ()=>{
        while(searchFrm.firstChild){
            searchFrm.removeChild(searchFrm.firstChild);
        }
        searchFrm.submit();
    });




}; // end of window.onloag = function(){}