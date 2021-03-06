package org.whiteboard.server.service;

import javafx.scene.input.DataFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Service;
import org.whiteboard.server.dao.MeetingDao;
import org.whiteboard.server.dao.UserDao;
import org.whiteboard.server.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 李浩然 on 2017/5/15.
 */
@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingDao meetingDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WhiteboardService whiteboardService;

    private Logger logger = Logger.getLogger(MeetingServiceImpl.class);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private MeetingManager meetingManager = MeetingManager.getInstance();

    @Override
    public Meeting initMeeting(String meetingName, long organizerId, int maxPartnerNumber, String roomPassword) {
        List<Long> partnerIds = new ArrayList<>();
        partnerIds.add(organizerId);
        Meeting meeting = new Meeting(DEFAULT_MEETING_ID, meetingName, DEFAULT_PARTNER_NUMBER, organizerId, new Date(),
                new Date(), DEFAULT_NOTE_PATH, meetingManager.generateRoomId(), partnerIds, maxPartnerNumber, roomPassword);
        if (!meetingManager.addMeeting(meeting)) {
            meeting = null;
        }
        return meeting;
    }

    @Override
    public void startMeeting(int roomId) {
        meetingManager.startMeeting(roomId);
    }

    @Override
    public Meeting finishAndSaveMeeting(int roomId) {
        Meeting meeting = meetingManager.getMeeting(roomId);
        meeting.setEndTime(new Date());
        meeting = meetingDao.addMeetingToDB(meeting);
        logger.info("result: " + meeting.getMeetingId());
        whiteboardService.setMeetingId(meeting.getMeetingId(), roomId);
        logger.info("save whiteboards....");
        whiteboardService.saveWhiteboards(roomId);
        logger.info("remove meeting...");
        meetingManager.removeMeeting(roomId);
        return meeting;
    }

    @Override
    public boolean destroyMeeting(int roomId) {
        return meetingManager.removeMeeting(roomId);
    }

    @Override
    public boolean joinMeeting(long userId, int roomId, String password) {
        return meetingManager.addUserToMeeting(userId, roomId, password);
    }

    @Override
    public boolean quitMeeting(long userId, int roomId) {
        return meetingManager.removeUserFromMeeting(userId, roomId);
    }

    @Override
    public boolean enterMeeting(long userId) {
        return meetingManager.enterRoom(userId);
    }

    @Override
    public boolean leaveMeeting(long userId) {
        return meetingManager.leaveRoom(userId);
    }

    @Override
    public int getRoomIdByUserId(long userId) {
        return meetingManager.getRoomId(userId);
    }

    @Override
    public Meeting getRunningMeetingByUserId(long userId) {
        return meetingManager.getMeeting(meetingManager.getRoomId(userId));
    }

    @Override
    public JSONObject getMeetingJSON(Meeting meeting) {
        if (meeting == null){
            return new JSONObject();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("meeting_id", meeting.getMeetingId());
        jsonObject.put("meeting_name", meeting.getMeetingName());
        jsonObject.put("partner_number", meeting.getPartnerNumber());
        jsonObject.put("organizer", userDao.findUserByIdFromDB(meeting.getOrganizerId()).getUsername());
        jsonObject.put("start_time", dateFormat.format(meeting.getStartTime()));
        jsonObject.put("end_time", dateFormat.format(meeting.getEndTime()));
        jsonObject.put("room_id", meeting.getMeetingRoomId());
        jsonObject.put("status", meeting.isStarted());

        JSONArray jsonArray = new JSONArray();
        for(long partnerId : meeting.getPartnerIds()) {
            JSONObject tmp = new JSONObject();
            tmp.put("username", userDao.findUserByIdFromDB(partnerId).getUsername());
            jsonArray.add(tmp);
        }
        jsonObject.put("partners", jsonArray);
        return jsonObject;
    }

    @Override
    public Meeting getRunningMeetingByRoomId(int roomId) {
        return meetingManager.getMeeting(roomId);
    }

    @Override
    public Meeting getMeetingById(long meetingId) {
        return meetingDao.findMeetingById(meetingId);
    }

    @Override
    public List<Meeting> getMeetingsByUserId(long userId) {
        List<Long> meetingIds = meetingDao.findMeetingIdsByUserId(userId);
        List<Meeting> meetings = new ArrayList<>();
        for(long meetingId : meetingIds) {
            meetings.add(meetingDao.findMeetingById(meetingId));
        }
        return meetings;
    }
}

