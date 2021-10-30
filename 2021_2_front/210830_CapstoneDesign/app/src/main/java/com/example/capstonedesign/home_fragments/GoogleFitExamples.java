package com.example.capstonedesign.home_fragments;

public class GoogleFitExamples {
}

/*

public void readSpeedData(Context appContext, TextView textView, FitnessOptions fitnessOptions,OnDataPointListener[] ListenerManager){
        Log.d("readData4","here4");
        ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        DataReadRequest request = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_SPEED)
                .bucketByTime(1,TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(),TimeUnit.SECONDS)
                .build();

        Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(appContext))
                .readData(request)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.d("AGGREGATE_SPEED : ","process name getHistoryClient has been successfully done");
                        for(Bucket bucket : dataReadResponse.getBuckets()){
                            Log.d("Bucket reading :", "Success");
                            for(DataSet dataset : bucket.getDataSets()){
                                Log.d("DataSet reading :", "Success");
                                if(dataset.getDataPoints().isEmpty()) Log.d("DataPoint","Empty");
                                for(DataPoint dp : dataset.getDataPoints()){
                                    Log.d("DataPoint reading :", "Success");
                                    for(Field field : dp.getDataType().getFields()){
                                        Log.d("Field reading :", "Success");
                                        if("average".equals(field.getName())){
                                            userInputSpeed = Math.round(dp.getValue(field).asFloat());
                                        }
                                    }
                                }
                            }
                        }
                        textView.setText("평균 속도 : " + String.valueOf(userInputSpeed)+" m/s");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("readData4","Here's some problem of getting history client");
                    }
                });


        OnDataPointListener listener = dataPoint -> {
            for(Field field: dataPoint.getDataType().getFields()) {
                Log.d("Field Name : ", field.getName());
                if ("average".equals(field.getName())) {
                    float avg_speed = dataPoint.getValue(field).asFloat();
                    userInputSpeed += avg_speed;
                }
            }
            textView.setText("평균 속도 : " + String.valueOf(userInputSpeed) +" m/s");
        };
        ListenerManager[SPD_LISTENER] = listener;

        Fitness.getSensorsClient(getContext(), GoogleSignIn.getAccountForExtension(appContext, fitnessOptions))
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_SPEED) // Can't be omitted.
                                .setSamplingRate(1, TimeUnit.SECONDS)
                                .build(),
                        listener
                )
                .addOnSuccessListener(unused ->
                        Log.i(TAG, "Listener registered!"))
                .addOnFailureListener(task ->
                        Log.e(TAG, "Listener not registered.", task.getCause()));
    }
 */
