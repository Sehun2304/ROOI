package com.rooi.rooi.controller;

import com.rooi.rooi.dto.BoardResponseDto;
import com.rooi.rooi.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {

    @Autowired
    private BoardService boardService;

    //보드를 생성함면 보이는 보드페이지 이때 주소는 /boards/{id} 이지만 데이터는 해당 보드의 칼럼과 카드를 모두 가져온다
    @GetMapping("/boards/{id}")
    public String boardsView(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "Index";
    }

	// 보드 생성 페이지
//	@GetMapping("/boards/create")
//	public String createBoardPage() {
//		return "createboard";
//	}

	// 보드 생성 페이지
	@GetMapping("/boards/create")
	public String createBoard() {
		return "createboard";
	}

	// 보드 수정 페이지
	@GetMapping("/boards/update/{id}")
	public String updateBoardPage(@PathVariable Long id, Model model) {
		model.addAttribute("id", id);
		return "updateboard";
	}

	// 보드 삭제 페이지 (미구현)
	@GetMapping("/boards/delete/{id}")
	public String deleteBoardPage(@PathVariable Long id, Model model) {
		model.addAttribute("id", id);
		return "deleteboard";
	}

	//칼럼을 수정할때 1차적으로 기존 칼럼의 데이터를 가져오는 겟메서드, 이때 특정 칼럼을 조회하면 그 칼럼의 카드는 모두 조회한다.
	@GetMapping("/board/{id}/columns/{columnsId}")
	public String columnsView() {
		return "columns";
	}

    //칼럼에서 수정할때 카드들도 수정하기 때문에 이 메서드가 필요한지 모르겠다.
    @GetMapping("/cards")
    public String cardsView() {
        return "cards";
    }

    //특정 아이디 값을 같은 유저를 초대하는 메서드로 보인다.
    @GetMapping("/invite/{id}")
    public String inviteUserPage(@PathVariable Long id, Model model){
        model.addAttribute("boardId", id);
        return "inviteUser";
    }
}
