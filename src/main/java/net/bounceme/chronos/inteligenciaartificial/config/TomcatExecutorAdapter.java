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
		log.debug("Execute {}", command);
		executorService.execute(command);
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return null;
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
	}

	@Override
	public void init() throws LifecycleException {
		log.debug("init");
	}

	@Override
	public void start() throws LifecycleException {
		log.debug("destroy");
	}

	@Override
	public void stop() throws LifecycleException {
		log.debug("stop");
	}

	@Override
	public void destroy() throws LifecycleException {
		log.debug("destroy");
	}

	@Override
	public LifecycleState getState() {
		return null;
	}

	@Override
	public String getStateName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}
}
