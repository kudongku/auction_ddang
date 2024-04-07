package com.ip.ddangddangddang.domain.comment.repository;

import com.ip.ddangddangddang.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuctionIdOrderByCreatedAt(Long auctionId);

}
