package bunlisugo.client.controller;

import java.util.List;
import java.awt.Rectangle;
import java.sql.SQLException;

import bunlisugo.server.dto.TrashDTO;
import bunlisugo.server.service.TimerManager;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class GameController {
    private List<TrashDTO> activeTrashes;
    private TimerManager timerManager;

    private String currentServerTime = "00:00"; 
    private TimePanel timePanel; // TimePanel 참조 추가
    private TrashBoxPanel trashBoxPanel;


    //얘가 연결해주는 역할 
    public void setTimePanel(TimePanel timePanel) {
        this.timePanel = timePanel;
    }
    //서버에서 오는 시간 정보를 받는 곳. 
    public void updateTime(String serverTime) {
        this.currentServerTime = serverTime;
        if (timePanel != null) {
            // timePanel.updateTimeDisplay(serverTime); // TimePanel에 시간 업데이트 메서드 호출
            timePanel.updateTimeLabel(serverTime);
        
        }
    }

    //view는 여기서 가져감. 
    public String getCurrentTime(){
        return this.currentServerTime;
    }

    //서버로 보내줄 TrashBox 경계 정보 
    public Rectangle getTrashBoxBounds(){
        return trashBoxPanel.getTrashBoxBounds();
    }
    
    public void setTrashBoxPanel(TrashBoxPanel trashBoxPanel) {
        this.trashBoxPanel = trashBoxPanel;
    }
   
    
   



    // public List<Trash> getRandomTrashes(int count) throws SQLException {
    //     return trashDAO.findRandom(count);
    // }   

}