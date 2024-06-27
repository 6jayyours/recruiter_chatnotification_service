package com.recruiter.chatnotification_service.repository;

import com.recruiter.chatnotification_service.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Message, Integer> {
    List<Message> findByChatId(String s);

    @Query("SELECT DISTINCT m.receiverId FROM Message m WHERE m.senderId = :senderId")
    List<Integer> findDistinctReceiverIdsBySenderId(@Param("senderId") Integer senderId);

    List<Message> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
}
