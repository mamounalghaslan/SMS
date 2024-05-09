package mgkm.smsbackend.jobsConfigs;

import mgkm.smsbackend.jobsConfigs.inference.InferenceTaskletsConfig;
import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.jobsConfigs.training.TrainingTaskletsConfig;
import mgkm.smsbackend.models.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Configuration
@EnableScheduling
public class JobConfig {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> inferenceFutureJob = null;
    private ScheduledFuture<?> trainingFutureJob = null;

    private final InferenceTaskletsConfig inferenceTaskletsConfig;
    private final TrainingTaskletsConfig trainingTaskletsConfig;

    public JobConfig(JobLauncher jobLauncher,
                     JobRepository jobRepository,
                     TaskScheduler taskScheduler,
                     PlatformTransactionManager transactionManager,
                     InferenceTaskletsConfig inferenceTaskletsConfig,
                     TrainingTaskletsConfig trainingTaskletsConfig) {
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.taskScheduler = taskScheduler;
        this.transactionManager = transactionManager;
        this.inferenceTaskletsConfig = inferenceTaskletsConfig;
        this.trainingTaskletsConfig = trainingTaskletsConfig;
    }

    // Inference -----------------------------------------------------------------------------------------

    public String startInferenceJob(Model model) {

        if (this.inferenceFutureJob != null && !this.inferenceFutureJob.isDone()) {
            log.info("Inference Job already running!");
            return "Inference Job already running!";
        }

        this.inferenceFutureJob = this.taskScheduler.scheduleWithFixedDelay(
                () -> {
                    try {
                        this.jobLauncher.run(
                                this.inferenceTaskletsConfig.inferenceJob(
                                        this.jobRepository, this.transactionManager, model),
                                new JobParametersBuilder()
                                        .addString("JobID", String.valueOf(new Date().getTime()))
                                        .toJobParameters());
                    } catch (Exception e) {
                        log.error(e.toString());
                        throw new RuntimeException(e);
                    }
                },
                Instant.now(),
                Duration.ofSeconds(10));

        log.info("Inference Job scheduled.");
        return "Inference Job scheduled.";
    }

    public String stopInferenceJob() {

        if (this.inferenceFutureJob != null && !this.inferenceFutureJob.isCancelled()) {

            this.inferenceFutureJob.cancel(true);

            log.info("Inference Job stopped.");
            return "Inference Job stopped.";
        }

        return "No inference job to stop!";
    }

    public Boolean isInferenceJobRunning() {
        return this.inferenceFutureJob != null && !this.inferenceFutureJob.isDone();
    }

    // Training -----------------------------------------------------------------------------------------

    public String startTrainingJob(String backboneName) {

        if (this.trainingFutureJob != null && !this.trainingFutureJob.isDone()) {
            log.info("Training Job already running!");
            return "Training Job already running!";
        }

        this.trainingFutureJob = this.taskScheduler.schedule(
                () -> {
                    try {
                        this.jobLauncher.run(
                                this.trainingTaskletsConfig.trainingJob(
                                        this.jobRepository,
                                        this.transactionManager,
                                        backboneName),
                                new JobParametersBuilder()
                                        .addString("JobID", String.valueOf(new Date().getTime()))
                                        .toJobParameters());
                    } catch (Exception e) {
                        log.error(e.toString());
                        throw new RuntimeException(e);
                    }
                },
                Instant.now());

        log.info("Training Job started.");
        return "Training Job started.";
    }

    public String stopTrainingJob() {

        if (this.trainingFutureJob != null && !this.trainingFutureJob.isCancelled()) {

            this.trainingFutureJob.cancel(true);

            log.info("Training Job stopped.");
            return "Training Job stopped.";
        }

        return "No training job to stop!";
    }

    public Boolean isTrainingJobRunning() {
        return this.trainingFutureJob != null && !this.trainingFutureJob.isDone();
    }

}
