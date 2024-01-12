package com.example.guestbook.controller;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }//public String list() ends

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list.........." + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
    }//public String list() ends

    /******
     * 게시글 등록을 누르면, 게시글을 입력할 수 있는 페이지로 이동한다
     * *****/
    @GetMapping("/register")
    public void register(){
        log.info("register get............");
    } // end of register()

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto..." + dto);
        Long gno = service.register(dto);
        // addFlashAttribute는 단 한 번만 데이터를 전달하는 용도로 사용한다.
        // redirectAttributes를 이용해서 한 번만 화면에서 "msg"라는 변수를 사용할 수 있도록 한다.
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    } // end of registerPost()

    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno: " + gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    } // end of read()

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        log.info("post modify..............................");
        log.info("modify dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";
    } // end of modify()

    @PostMapping("/remove")
    public String remove(Long gno, RedirectAttributes redirectAttributes){
        log.info("remove gno: " + gno);
        service.remove(gno);

        // 글 등록 후에, list로 돌아갈 때
        // 글이 등록됐다는 알림창(modal)을 띄워준다.
        // modal이 띄워지는 조건을 교재에서는 list.html에 삽입했으나
        // 나는 js파일로 나누고 싶어서 입력 안했다.
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    } // end of remove()
} // public class GuestbookController ends
