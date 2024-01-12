window.onload = function(){
    // 게시글 수정 페이지 내 요소
    const modifyFrm = document.getElementById("modifyForm");
    const removeBtn = document.querySelector(".removeBtn");
    const modifyBtn = document.querySelector(".modifyBtn");
    const listBtn = document.querySelector(".listBtn");

    // 게시글 삭제 버튼 클릭 이벤트
    removeBtn.addEventListener("click", ()=>{
        // console.log("find me");
        modifyFrm.setAttribute("action", "/guestbook/remove");
        modifyFrm.setAttribute("method", "post");
        modifyFrm.submit();
    });


    // 게시글 수정 버튼 클릭 이벤트
    modifyBtn.addEventListener("click", ()=>{
        console.log("I'm modify Btn");
        if(!confirm("Editing content")) { return ; }
        modifyFrm.setAttribute("action", "/guestbook/modify");
        modifyFrm.setAttribute("method", "post");
        modifyFrm.submit();
    });


    // 게시글 수정 후 게시글 목록으로 돌아가는 버튼 클릭 이벤트
    // modify.html에서 list로 돌아가는 버튼에 a태그를 달지 않았을 경우 사용
    listBtn.addEventListener("click", ()=>{
        const page = document.querySelector("#page");
        const type = document.querySelector("#type");
        const keyword = document.querySelector("#keyword");

        while(modifyFrm.firstChild){
            modifyFrm.removeChild(modifyFrm.firstChild);
        }
        modifyFrm.insertAdjacentElement("beforeend", page);
        modifyFrm.insertAdjacentElement("beforeend", type);
        modifyFrm.insertAdjacentElement("beforeend", keyword);
        modifyFrm.setAttribute("action", "/guestbook/list");
        modifyFrm.setAttribute("method", "get");

        // console.log(modifyFrm.innerHTML); // <input type="hidden" name="page" id="page" value="1">
        modifyFrm.submit();
    });


}; // end of window.onloag = function(){}