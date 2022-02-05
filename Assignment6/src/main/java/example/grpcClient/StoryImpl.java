package example.grpcClient;

import io.grpc.stub.StreamObserver;
import service.*;


// Implement the joke service. It has two services getJokes and setJoke
class StoryImpl extends StoryGrpc.StoryImplBase {

    String story;

    public StoryImpl(){
        super();
        // Add the beginning of the story
        story = "Once upon a time, ";
    }

    @Override
    public void read(Empty request, StreamObserver<ReadResponse> responseObserver) {
        ReadResponse response = ReadResponse.newBuilder().setSentence(story).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void write(WriteRequest request, StreamObserver<WriteResponse> responseObserver) {
        String strToAdd = request.getNewSentence();
        story = story + strToAdd;
        WriteResponse response = WriteResponse.newBuilder().setIsSuccess(true).setStory(story).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}