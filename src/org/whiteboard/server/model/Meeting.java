package org.whiteboard.server.model;

import com.sun.xml.internal.bind.v2.model.core.ID;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 李浩然 on 2017/5/15.
 */
@Entity
@Table(name = "table_meeting")
public class Meeting {
    @Column(name = "meeting_id")
    private long meetingId;
    @Column(name = "meeting_name")
    private String meetingName;
    @Column(name = "partner_number")
    private int partnerNumber;
    @Column(name = "organizer_id")
    private long organizerId;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "note_path")
    private String notePath;
    @Column(name = "meeting_room_id")
    private int meetingRoomId;

    private List<Long> partnerIds;
    private int maxPartnerNumber;
    private boolean isStarted;
    private String roomPassword;

    public Meeting() {
        this.partnerIds = new ArrayList<>();
    }

    public Meeting(long meetingId, String meetingName, int partnerNumber, long organizerId,
                   Date startTime, Date endTime, String notePath, int meetingRoomId) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.partnerNumber = partnerNumber;
        this.organizerId = organizerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notePath = notePath;
        this.meetingRoomId = meetingRoomId;
        isStarted = false;
    }

    public Meeting(long meetingId, String meetingName, int partnerNumber, long organizerId, Date startTime, Date endTime,
                   String notePath, int meetingRoomId, List<Long> partnerIds, int maxPartnerNumber, String roomPassword) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.partnerNumber = partnerNumber;
        this.organizerId = organizerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notePath = notePath;
        this.meetingRoomId = meetingRoomId;
        this.partnerIds = partnerIds;
        this.maxPartnerNumber = maxPartnerNumber;
        this.roomPassword = roomPassword;
        isStarted = false;
    }

    public long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(long meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public int getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(int partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    public long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(long organizerId) {
        this.organizerId = organizerId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getNotePath() {
        return notePath;
    }

    public void setNotePath(String notePath) {
        this.notePath = notePath;
    }

    public int getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(int meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public List<Long> getPartnerIds() {
        return partnerIds;
    }

    public void setPartnerIds(List<Long> partnerIds) {
        this.partnerIds = partnerIds;
    }

    public boolean addPartner(long partnerId) {
        if (partnerNumber < maxPartnerNumber && !isPartner(partnerId)) {
            partnerIds.add(partnerId);
            partnerNumber++;
            return true;
        } else {
            return false;
        }
    }

    public boolean removePartner(long partnerId) {
        if (isPartner(partnerId)) {
            partnerIds.remove(partnerId);
            partnerNumber--;
            return true;
        } else {
            return false;
        }
    }

    public int getMaxPartnerNumber() {
        return maxPartnerNumber;
    }

    public void setMaxPartnerNumber(int maxPartnerNumber) {
        this.maxPartnerNumber = maxPartnerNumber;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isPartner(long userId) {
        for (long id : partnerIds) {
            if (id == userId) {
                return true;
            }
        }
        return false;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }
}
