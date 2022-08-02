package com.learn.spring.batch.one.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.repository.UserRepo;

import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class UserConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private UserRepo repo;
	
	@Bean
	public FlatFileItemReader<UserData> reader(){
		FlatFileItemReader<UserData> itemReader =new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/onemusers.csv"));
		itemReader.setName("csvReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	private LineMapper<UserData> lineMapper() {
		DefaultLineMapper<UserData> lineMapper = new DefaultLineMapper<UserData>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("name","age","addressone","addresstwo");
		BeanWrapperFieldSetMapper<UserData> fieldMapper = new BeanWrapperFieldSetMapper<UserData>();
		fieldMapper.setTargetType(UserData.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldMapper);
		return lineMapper;
	}
	
	@Bean
	public UserProcessor processor() {
		return new UserProcessor();
	}
	
	public RepositoryItemWriter<UserData> writer(){
		RepositoryItemWriter<UserData> writer = new RepositoryItemWriter<UserData>();
		writer.setRepository(repo);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("csv-step").<UserData,UserData>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("importUserInfo")
				.flow(step1())
				.end()
				.build();
	}
	
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(100);
		return asyncTaskExecutor;
	}
}
