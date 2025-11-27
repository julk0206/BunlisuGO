package bunlisugo.server.service;

import java.awt.Rectangle;

import bunlisugo.server.dto.TrashDTO;


//trash가 boxPanel에 포함되었는지 판단하는 서비스
public class TrashJudgeService {
    //trashDTO에서 Trash의 x,y 받아올거임. 
    //boxPanel에 저 trash들이 포함되었는지 판단하는 메소드 작성. 
    //결과값으로는 isCollected변경하는 것까지. 

    private TrashDTO trash;
    public boolean judgeTrashCollected(int trashX, int trashY) {


        //trashDTO에서 좌표 받아오기
        trashX = trash.getX();
        trashY = trash.getY();

        //클라에서 이거 불러와야됨
        Rectangle trashBoxBounds = new Rectangle(120, 484, 756, 217); 



        // 쓰레기가 박스 안에 있는지 확인
        if(trashBoxBounds.contains(trashX, trashY)){//박스 포함되는 것 

            //trash.setIsCollected(true);
            return true;
        }
        
        return false; //
    // 포함 여부 반환
    //set 설정하는 것. 바꾸는 것 isCollected를 바꾸는 메소드 
}

}