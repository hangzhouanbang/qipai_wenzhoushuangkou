package com.anbang.qipai.wenzhoushuangkou.cqrs.c.domain;

public class ReadyForGameResult {
	private PukeGameValueObject pukeGame;

	public PukeGameValueObject getPukeGame() {
		return pukeGame;
	}

	public void setPukeGame(PukeGameValueObject pukeGame) {
		this.pukeGame = pukeGame;
	}

}
