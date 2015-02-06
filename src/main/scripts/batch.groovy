import org.grails.boot.internal.EnableAutoConfiguration
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

description "Example Batch", "grails batch"

println "Example Batch"

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
                return null
                }
            }
        ).build();
    }

    @Bean
    public Job job(Step step1) throws Exception {
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }
}


public class Main {
    public static void main(String [] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(
                BatchConfiguration.class, args)));
    }
}


String[] args=[]
Main.main(args)