package example.grpcClient;

import io.grpc.stub.StreamObserver;
import service.*;

import java.util.ArrayList;
import java.util.List;

// Implement the golf service.
class GolfImpl extends GolfGrpc.GolfImplBase {

	List<CourseScoreEntry> courseScores;

	public GolfImpl() {
		super();
		courseScores = new ArrayList<>();
	}

	@Override
	public void recordScore(RecordScoreReq request, StreamObserver<RecordScoreRes> responseObserver) {
		RecordScoreRes.Builder response = RecordScoreRes.newBuilder();
		CourseScoreEntry.Builder entry = CourseScoreEntry.newBuilder();
		boolean validRequest;
		if (!request.getName().isEmpty()) {
			entry.setName(request.getName());
			validRequest = true;
		} else {
			response.setIsSuccess(false).setError("Error with name");
			validRequest = false;
		}

		if (!request.getCourseName().isEmpty()) {
			entry.setCourseName(request.getCourseName());
			validRequest = true;
		} else {
			response.setIsSuccess(false).setError("Error with course name");
			validRequest = false;
		}

		if (!request.getDate().isEmpty()) {
			entry.setDate(request.getDate());
			validRequest = true;
		} else {
			response.setIsSuccess(false).setError("Error with date recorded");
			validRequest = false;
		}

		if (!(request.getScore() <= 0)) {
			entry.setScore(request.getScore());
			validRequest = true;
		} else {
			response.setIsSuccess(false).setError("Error with score value");
			validRequest = false;
		}

		if (validRequest) {
			courseScores.add(entry.build());
			response.setIsSuccess(true).setMessage(request.getName() + " your score of " + request.getScore() + " at " + request.getCourseName() + " has been posted");
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getAllScores(NoMessage request, StreamObserver<AllScoresRes> responseObserver) {
		AllScoresRes.Builder response = AllScoresRes.newBuilder();

		if (!courseScores.isEmpty()) {
			response.addAllScores(courseScores).setIsSuccess(true);
		} else {
			response.setIsSuccess(false).setError("No scores recorded");
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();

	}


}