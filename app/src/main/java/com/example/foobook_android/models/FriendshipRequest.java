package com.example.foobook_android.models;

// Represents a friendship request between two users, identifying them by their user IDs.
public class FriendshipRequest {
    private final String requesterId; // The ID of the user sending the friendship request.
    private final String receiverId; // The ID of the user receiving the friendship request.

    // Constructs a new FriendshipRequest with the IDs of the requester and receiver.
    public FriendshipRequest(String requesterId, String receiverId) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
    }

    // Retrieves the ID of the request sender.
    public String getRequesterId() {
        return requesterId;
    }

    // Retrieves the ID of the request receiver.
    public String getReceiverId() {
        return receiverId;
    }
}
