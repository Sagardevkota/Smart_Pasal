package com.example.smartpasal.model;


    public class Feedback {

        private Integer id;

        private Integer user_id;

        private String subject;

        private String message;


        public Feedback(Integer user_id,String subject, String message) {
            this.user_id = user_id;
            this.subject=subject;
            this.message = message;
        }

        public Feedback() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUser_id() {
            return user_id;
        }


        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


