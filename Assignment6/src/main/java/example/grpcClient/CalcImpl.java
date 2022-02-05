package example.grpcClient;

import io.grpc.stub.StreamObserver;
import service.CalcGrpc;
import service.CalcRequest;
import service.CalcResponse;

// Implement the joke service. It has two services getJokes and setJoke
class CalcImpl extends CalcGrpc.CalcImplBase {

    public CalcImpl(){
        super();
        // copying some dad jokes
    }

    @Override
    public void add(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        CalcResponse.Builder response = CalcResponse.newBuilder();
        //Get values
        if (request.getNumList().isEmpty()) {
            response.setError("Empty Request Values.").setIsSuccess(false);
        } else if (request.getNumCount() == 1) {
            response.setError("Only one value was provided.").setIsSuccess(false);
        } else {
            //get all the numbers
            int counter= 0;
            double sum = 0;
            System.out.println(request.getNumCount());
            while (counter < request.getNumCount()) {
                sum = sum + request.getNum(counter);
                counter++;
            }
            response.setSolution(sum).setIsSuccess(true);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void subtract(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        CalcResponse.Builder response = CalcResponse.newBuilder();
        //Get values
        if (request.getNumList().isEmpty()) {
            response.setError("Empty Request Values").setIsSuccess(false);
        } else if (request.getNumCount() == 1) {
            response.setError("Only one value was provided").setIsSuccess(false);
        } else {
            //get all the numbers
            int counter= 0;
            double difference = 0;
            while (counter < request.getNumCount()) {
                if (counter == 0) {
                    difference = request.getNum(counter);
                } else {
                    difference = difference - request.getNum(counter);
                }
                counter++;
            }
            response.setSolution(difference).setIsSuccess(true);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void   multiply(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        CalcResponse.Builder response = CalcResponse.newBuilder();
        //Get values
        if (request.getNumList().isEmpty()) {
            response.setError("Empty Request Values").setIsSuccess(false);
        } else if (request.getNumCount() == 1) {
            response.setError("Only one value was provided").setIsSuccess(false);
        } else {
            //get all the numbers
            int counter= 0;
            double product = 0;
            while (counter < request.getNumCount()) {
                if (counter == 0) {
                    product = request.getNum(counter);
                } else {
                    product = product * request.getNum(counter);
                }
                counter++;
            }
            response.setSolution(product).setIsSuccess(true);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void divide(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        CalcResponse.Builder response = CalcResponse.newBuilder();
        //Get values
        if (request.getNumList().isEmpty()) {
            response.setError("Empty Request Values").setIsSuccess(false);
        } else if (request.getNumCount() == 1) {
            response.setError("Only one value was provided").setIsSuccess(false);
        } else {
            try {
                //get all the numbers
                int counter=  0;
                double value = 0;
                while (counter < request.getNumCount()) {
                    if (counter == 0) {
                        value = request.getNum(counter);
                    } else {
                        value = value / request.getNum(counter);
                    }
                    counter++;
                }
                response.setSolution(value).setIsSuccess(true);
            } catch (ArithmeticException e) {
                response.setError("Arithmetic Error").setIsSuccess(false);
            }
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }




}