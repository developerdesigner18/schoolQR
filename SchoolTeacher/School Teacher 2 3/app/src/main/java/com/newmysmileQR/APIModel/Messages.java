package com.newmysmileQR.APIModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Messages {

    @SerializedName("message-count")
    @Expose
    private String messageCount;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    class Message {
        @SerializedName("to")
        @Expose
        private String to;
        @SerializedName("message-id")
        @Expose
        private String messageId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("remaining-balance")
        @Expose
        private String remainingBalance;
        @SerializedName("message-price")
        @Expose
        private String messagePrice;
        @SerializedName("network")
        @Expose
        private String network;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemainingBalance() {
            return remainingBalance;
        }

        public void setRemainingBalance(String remainingBalance) {
            this.remainingBalance = remainingBalance;
        }

        public String getMessagePrice() {
            return messagePrice;
        }

        public void setMessagePrice(String messagePrice) {
            this.messagePrice = messagePrice;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

    }

}
