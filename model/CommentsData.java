package com.example.reader20.model;

import java.util.List;

/**
 * Created by 27721_000 on 2016/8/23.
 */
public class CommentsData {

private List<comments> comments;

    public List<CommentsData.comments> getComments() {
        return comments;
    }

    public void setComments(List<CommentsData.comments> comments) {
        this.comments = comments;
    }

    public static class comments{
        private String id;  //评论用户的id
        private String author;  //评论用户
        private String content ;  //评论内容
        private String likes ;    //评论获得赞数
        private String avatar;  //评论用户头像的图片地址
        private String time ;  //评论的时间
        private ReplyToBean reply_to; //下面的回复

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public static class ReplyToBean{
            private String id;
            private String status;
            private String author;
            private String content;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
