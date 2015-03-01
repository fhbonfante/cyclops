package com.aol.simple.react.eager;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;

import com.aol.simple.react.base.BaseSequentialSeqTest;
import com.aol.simple.react.stream.eager.EagerFutureStream;
import com.aol.simple.react.stream.traits.FutureStream;

public class EagerSequentialSeqTest extends BaseSequentialSeqTest {

	@Override
	protected <U> EagerFutureStream<U> of(U... array) {
		return EagerFutureStream.sequentialBuilder().of(array);
	}

	@Test
	public void testLimitFutures(){
		assertThat(of(1,2,3,4,5).limitFutures(2).collect(Collectors.toList()),is(asList(1,2)));
		
	}	
	@Test
	public void testSkipFutures(){
		assertThat(of(1,2,3,4,5).skipFutures(2).collect(Collectors.toList()),is(asList(3,4,5)));
	}
	@Test
	public void testSliceFutures(){
		assertThat(of(1,2,3,4,5).sliceFutures(3,4).collect(Collectors.toList()),is(asList(4)));
	}
	@Test
	public void testSplitFuturesAt(){
		assertThat(of(1,2,3,4,5).splitAtNonBlockingFutureStream(2).v1.block(),is(asList(1,2)));
	}
	@Test
	public void testSplitFuturesAt2(){
		assertThat(of(1,2,3,4,5).splitAtNonBlockingFutureStream(2).v2.collect(Collectors.toList()),is(asList(3,4,5)));
	}

	
	@Test
	public void testZipWithFutures(){
		Stream stream = of("a","b");
		Seq<Tuple2<CompletableFuture<Integer>,String>> seq = of(1,2).zipFutures(stream);
		List<Tuple2<Integer,String>> result = seq.map(tuple -> Tuple.tuple(tuple.v1.join(),tuple.v2)).collect(Collectors.toList());
		assertThat(result,is(asList(tuple(1,"a"),tuple(2,"b"))));
	}
	

	@Test
	public void testZipFuturesWithIndex(){
		
		Seq<Tuple2<CompletableFuture<String>,Long>> seq = of("a","b").zipFuturesWithIndex();
		List<Tuple2<String,Long>> result = seq.map(tuple -> Tuple.tuple(tuple.v1.join(),tuple.v2)).collect(Collectors.toList());
		assertThat(result,is(asList(tuple("a",0l),tuple("b",1l))));
	}
	@Test
	public void duplicateFutures(){
		List<String> list = of("a","b").duplicateFuturesFutureStream().v1.block();
		assertThat(list,is(asList("a","b")));
	}
	@Test
	public void duplicateFutures2(){
		List<String> list = of("a","b").duplicateFuturesFutureStream().v2.block();
		assertThat(list,is(asList("a","b")));
	}
	
}