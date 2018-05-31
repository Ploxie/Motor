package org.ploxie.engine.states;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine.rendering.Renderable;
import org.ploxie.engine.rendering.Updatable;


public class GameStateMachine implements Renderable, Updatable {

	/**
	 * Last element is on top
	 */
	private List<GameState> gameStatesStack = new ArrayList<>();

	public void pushGameState(final GameState gameState) {
		synchronized (gameStatesStack) {
			gameStatesStack.add(gameState);
		}
	}

	public void popGameState(final GameState gameState) {
		synchronized (gameStatesStack) {
			gameStatesStack.remove(gameState);
		}
	}

	@Override
	public void update(final float deltaTimeS) {
		synchronized (gameStatesStack) {
			for (GameState state : gameStatesStack) {
				state.update(deltaTimeS);
			}
		}
	}

	/**
	 * Single-threaded
	 */
	@Override
	public void render() {
		synchronized (gameStatesStack) {
			for (GameState state : gameStatesStack) {
				state.render();
			}
		}
	}

}