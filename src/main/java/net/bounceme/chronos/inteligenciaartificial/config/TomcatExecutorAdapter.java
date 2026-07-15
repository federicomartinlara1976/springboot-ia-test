package net.bounceme.chronos.inteligenciaartificial.config;

import java.util.concurrent.ExecutorService;

import org.apache.catalina.Executor;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TomcatExecutorAdapter implements Executor {

	private final ExecutorService executorService;

	public TomcatExecutorAdapter(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void execute(Runnable command) {
		executorService.execute(command);
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() throws LifecycleException {
		log.info("init");
	}

	@Override
	public void start() throws LifecycleException {
		log.info("destroy");
	}

	@Override
	public void stop() throws LifecycleException {
		log.info("stop");
	}

	@Override
	public void destroy() throws LifecycleException {
		log.info("destroy");
	}

	@Override
	public LifecycleState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
