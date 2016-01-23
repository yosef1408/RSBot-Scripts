package xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Action;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.Camera;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Interactive;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.LocalPath;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Player;
import org.powerbot.script.rt6.TilePath;
import org.powerbot.script.rt6.Widget;

import xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker.CBFMultitasker;

public class Methods extends ClientAccessor {

	private static final int randomInventInt = Random.nextInt(22, 27);

	public Methods(final ClientContext ctx) {
		super(ctx);
	}

	public void addBetterAxeToToolbelt() {
		ctx.backpack.select().id(getBetterAxeId()).poll()
				.interact("Add to tool belt");
	}

	public boolean addItem(final Component component) {
		final Action empty = ctx.combatBar.select().id(-1).poll();
		ctx.input.move(component.nextPoint());
		ctx.input.press(1);
		ctx.input.drag(empty.component().nextPoint(), 1);
		ctx.input.release(1);
		return ctx.combatBar.actionAt(empty.slot()).id() != -1;
	}

	public boolean addItem(final int itemId) {
		final Point pointOfRandomLog = ctx.backpack.id(itemId).shuffle().poll()
				.nextPoint();
		final int slot = ctx.combatBar.select().id(-1).first().poll().slot();
		final Point pointOfFirstAvailableSlot = ctx.combatBar.actionAt(slot)
				.component().nextPoint();
		ctx.input.move(pointOfRandomLog);
		Condition.wait(new Condition.Check() {

			@Override
			public boolean poll() {
				return ctx.input.getLocation().equals(pointOfRandomLog);
			}
		});
		ctx.input.press(MouseEvent.BUTTON1);
		ctx.input.move(pointOfFirstAvailableSlot);
		Condition.wait(new Condition.Check() {

			@Override
			public boolean poll() {
				return ctx.input.getLocation()
						.equals(pointOfFirstAvailableSlot);
			}
		});
		ctx.input.release(MouseEvent.BUTTON1);
		Condition.wait(new Condition.Check() {

			@Override
			public boolean poll() {
				return !ctx.combatBar.select().id(itemId).isEmpty();
			}
		});
		return ctx.combatBar.actionAt(slot).id() != -1;
	}

	public boolean addItem(final Item item) {
		return addItem(item.component());
	}

	public int[] allLogIds() {
		return new int[]{1511, 1515, 1517, 1519, 1521};
	}

	public int[] allNoteIds() {
		final int allIds[] = concat(allLogIds(), allUnstrungBowIds());
		for (int i = 0; i < allIds.length; i++) {
			if (allIds[i] == 52) {
				allIds[i] = allIds[i];
			} else {
				allIds[i] = (allIds[i] + 1);
			}
		}
		return allIds;
	}

	public int[] allUnstrungBowIds() {
		return new int[]{50, 52/* This is arrow shaft id */, 54, 60, 64, 68,
				72, 29736 /* End Shortbows */, 48, 56, 58, 62, 66, 70,
				29734 /* End shieldbows */, 9440, 9442, 9444, 9448, 9452, 25483};
	}

	public boolean bankItems(final String locString, final int[] itemIds) {
		final Component chooseWidget = ctx.widgets.widget(1179).component(30);
		if (chooseWidget.visible()) {
			ctx.input.send("{VK_ESCAPE}");
		} else {
			if (locString.equalsIgnoreCase("Port Sarim")) {
				final Widget bankWidget2 = ctx.widgets.widget(11);
				if (bankWidget2.valid()) {
					if (ctx.backpack.select().id(itemIds).count() > 0) {
						for (final int itemID : itemIds) {
							if (ctx.backpack.select().id(itemID).count() > 0) {
								final int assignmentItemIndex = ctx.backpack
										.select().id(itemID).shuffle().poll()
										.component().index();
								final Component component = bankWidget2
										.component(1).component(
												assignmentItemIndex);
								if (component.itemId() == (itemID)) {
									component.interact("Deposit-All");
									Condition.wait(new Callable<Boolean>() {
										@Override
										public Boolean call() {
											return Methods.this.ctx.backpack
													.select().id(itemID)
													.count() == 0;
										}
									}, 1000, 5);
								}
							}
						}
					} else {
						return true;
					}
				} else {
					final GameObject depositBox = ctx.objects.select(10)
							.id(36788).nearest().poll();
					if (depositBox.valid()) {
						if (depositBox.inViewport()) {
							depositBox.interact(true, "Deposit");
							Condition.wait(new Condition.Check() {

								@Override
								public boolean poll() {
									return bankWidget2.valid();
								}
							});
						} else {
							ctx.camera.turnTo(depositBox);
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return depositBox.inViewport();
								}
							}, 1000, 5);
						}
					}
				}
			} else {
				if (!ctx.bank.opened()) {
					final Npc bankers = ctx.npcs.select()
							.id(Constants.BANK_NPCS).limit(10).nearest().poll();
					if (!bankers.valid()) {
						if (ctx.bank.inViewport()) {
							ctx.bank.open();
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return Methods.this.ctx.bank.opened();
								}
							}, 2000, 5);
						} else {
							ctx.camera.turnTo(ctx.bank.nearest().tile());
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return Methods.this.ctx.bank.inViewport();
								}
							}, 1000, 5);
						}
					} else {
						bankers.interact("Bank");
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								return Methods.this.ctx.bank.opened()
										|| Methods.this.ctx.players.local()
												.inMotion();
							}
						}, 2000, 5);
					}
				} else {
					withdrawBestUsableAxe();
					if (hasUnusableAxe()) {
						depositUnusableAxe();
					}
					if (hasExtraAxeInInventory()) {
						depositExtraAxeInInventory();
					}
					if (ctx.backpack.select().id(itemIds).count() > 0) {
						if (ctx.backpack.select().count() == ctx.backpack
								.select().id(itemIds).count()) {
							ctx.bank.depositInventory();
						} else {
							for (final Item i : ctx.backpack.select()
									.id(itemIds).shuffle()) {
								final int itemID = i.id();
								ctx.bank.deposit(i.id(), Amount.ALL);
								Condition.wait(new Callable<Boolean>() {
									@Override
									public Boolean call() {
										return Methods.this.ctx.backpack
												.select().id(itemID).count() <= 0;
									}
								}, 500, 2);
							}
						}
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean bankLogs(final String typeSelection) {
		return typeSelection.contains("Bank");
	}

	public void burnLogs(final String treeType) {
		final int taskItemId = getTaskItemId(treeType);
		final Widget bankWidget2 = ctx.widgets.widget(11);
		if (bankWidget2.valid()) {
			ctx.input.send("{VK_ESCAPE}");
		} else {
			if (ctx.chat.queryContinue()) {
				ctx.chat.clickContinue(true);
			} else {
				if (ctx.combatBar.select().id(taskItemId).isEmpty()) {
					addItem(taskItemId);
				} else {
					final Widget fletchWidget1 = ctx.widgets.widget(1370);
					final Widget fletchWidget2 = ctx.widgets.widget(1371);
					if ((fletchWidget1.valid() && fletchWidget1.component(0)
							.visible())
							|| (fletchWidget2.valid() && fletchWidget2
									.component(0).visible())) {
						final Point closePoint = fletchWidget2.component(20)
								.nextPoint();
						ctx.input.move(closePoint);
						Condition.wait(new Condition.Check() {

							@Override
							public boolean poll() {
								return ctx.input.getLocation().equals(
										closePoint);
							}
						});
						ctx.input.click(true);
						Condition.wait(new Condition.Check() {

							@Override
							public boolean poll() {
								return !(fletchWidget1.valid() && fletchWidget1
										.component(0).visible())
										|| (fletchWidget2.valid() && fletchWidget2
												.component(0).visible());
							}
						});
					} else {
						final Widget toolWidget = ctx.widgets.widget(1179);
						final int slot = ctx.combatBar.poll().slot();
						final String slotBind = ctx.combatBar.actionAt(slot)
								.bind();
						final GameObject fireObj = ctx.objects.select(3)
								.name("Fire").nearest().poll();
						if (!fireObj.valid()) {
							if (toolWidget.component(39).text()
									.equalsIgnoreCase("Add to bonfire")) {
								ctx.input.send("1");
								Condition.wait(new Condition.Check() {
									@Override
									public boolean poll() {
										final Player p = Methods.this.ctx.players
												.local();
										return (Methods.this.ctx.backpack
												.select().count() == 0)
												&& p.idle()
												&& !p.inMotion()
												&& (p.animation() == -1);
									}
								});
							} else {
								ctx.input.send(slotBind);
								Condition.wait(new Condition.Check() {
									@Override
									public boolean poll() {
										return toolWidget
												.component(39)
												.text()
												.equalsIgnoreCase(
														"Add to bonfire");
									}
								});
							}
						} else {
							if (toolWidget.component(39).text()
									.equalsIgnoreCase("Add to bonfire")) {
								ctx.input.send("3");
								Condition.wait(new Condition.Check() {
									@Override
									public boolean poll() {
										final Player p = Methods.this.ctx.players
												.local();
										return (Methods.this.ctx.backpack
												.select().count() == 0)
												&& p.idle()
												&& !p.inMotion()
												&& (p.animation() == -1);
									}
								});
							} else {
								ctx.input.send(slotBind);
								Condition.wait(new Condition.Check() {
									@Override
									public boolean poll() {
										return toolWidget
												.component(39)
												.text()
												.equalsIgnoreCase(
														"Add to bonfire");
									}
								});
							}
						}
					}
				}
			}
		}
	}

	public boolean burnLogsMode(final String typeSelection) {
		return typeSelection.equalsIgnoreCase("Burn");
	}

	public void chop(final String treeType, final String locSelection,
			final Area taskArea) {
		if (ctx.widgets.widget(1184).component(11).text()
				.equalsIgnoreCase("Guard in tree")) {
			if (treeType.equalsIgnoreCase("Willow")
					&& locSelection.equalsIgnoreCase("Draynor Village")) {
				ctx.movement.step(ctx.players.local().tile().derive(2, 2));
				ctx.camera.angle('n');
			}
		}
		if (treeType.equalsIgnoreCase("Ivy")) {
			if (locSelection.equalsIgnoreCase("Varrock")) {
				if ((ctx.camera.yaw() < 134) || (ctx.camera.yaw() > 236)) {
					combineCamera(Random.nextInt(140, 230),
							Random.nextInt(9, 30));
					// ctx.camera.angle('s');
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() {
							final Camera c = ctx.camera;
							return (c.yaw() >= 134)
									|| (ctx.camera.yaw() <= 236);
						}
					}, 1000, 20);
				}
			} else {
				if (locSelection.equalsIgnoreCase("Falador")) {
					if ((ctx.camera.yaw() < 10) || (ctx.camera.yaw() > 80)) {
						combineCamera(Random.nextInt(14, 75),
								Random.nextInt(9, 30));
						// ctx.camera.angle('s');
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								final Camera c = ctx.camera;
								return (c.yaw() >= 10)
										|| (ctx.camera.yaw() <= 80);
							}
						}, 1000, 20);
					}
				}
			}
		}
		if (!treeType.equalsIgnoreCase("Ivy")) {
			if ((ctx.camera.pitch() < 15) || (ctx.camera.pitch() > 55)) {
				combineCamera(Random.nextInt(ctx.camera.yaw() - 5,
						ctx.camera.yaw() + 5), Random.nextInt(20, 50));
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						final Camera c = ctx.camera;
						return (c.pitch() >= 15) || (ctx.camera.pitch() <= 55);
					}
				}, 1000, 20);
			}
		} else {
			if ((ctx.camera.pitch() < 2) || (ctx.camera.pitch() > 18)) {
				combineCamera(Random.nextInt(ctx.camera.yaw() - 5,
						ctx.camera.yaw() + 5), Random.nextInt(4, 15));
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						final Camera c = ctx.camera;
						return (c.pitch() >= 2) || (ctx.camera.pitch() <= 18);
					}
				}, 1000, 20);
			}
		}
		GameObject objFinder = null;
		final int[] ivyBounds = {-128, 128, -450, -950, -128, 128};
		final int[] treeIds = getTaskTreeIds(treeType);
		if (!treeType.equalsIgnoreCase("Ivy")) {
			if (treeType.equalsIgnoreCase("Normal")) {
				objFinder = ctx.objects.select(10).id(treeIds).within(taskArea)
						.nearest().limit(10).poll();
			} else {
				objFinder = ctx.objects.select(15).id(treeIds).within(taskArea)
						.nearest().limit(5).poll();
			}
		} else {
			objFinder = ctx.objects.select(15).id(treeIds)
					.each(Interactive.doSetBounds(ivyBounds)).within(taskArea)
					.nearest().limit(3).poll();
		}
		final GameObject obj = objFinder;
		if (obj.valid()) {
			if (obj.inViewport()) {
				if (ctx.players.local().animation() == -1) {
					final Widget progressWidget = ctx.widgets.widget(1251);
					if (progressWidget.component(0).valid()
							&& progressWidget.component(0).visible()) {
						Condition.wait(new Condition.Check() {

							@Override
							public boolean poll() {
								return !progressWidget.component(0).visible();
							}
						});
					} else {
						final Component chooseWidget = ctx.widgets.widget(1179)
								.component(30);
						if (chooseWidget.visible()) {
							ctx.input.send("{VK_ESCAPE}");
						} else {
							if (treeType.equalsIgnoreCase("Ivy")) {
								final Player p = Methods.this.ctx.players
										.local();
								if (p.animation() == -1) {
									Condition.wait(
											new Condition.Check() {

												@Override
												public boolean poll() {
													return (p.animation() == -1)
															&& !p.inMotion()
															&& p.idle();
												}
											}, Random.nextInt(2500, 4000),
											Random.nextInt(5, 10));
									obj.interact(true, "Chop",
											getTaskTreeString(treeType));
									CBFMultitasker.runTries = 0;
								}
							} else {
								obj.interact(true, "Chop down",
										getTaskTreeString(treeType));
								CBFMultitasker.runTries = 0;
								Condition.wait(new Callable<Boolean>() {
									@Override
									public Boolean call() {
										final Player p = Methods.this.ctx.players
												.local();
										return (p.animation() == -1)
												&& !p.inMotion() && p.idle();
									}
								}, 1200, 10);
							}
						}
					}
				}
			} else {
				if (treeType.equalsIgnoreCase("Ivy")) {
					ctx.movement.step(obj.tile());
				} else {
					ctx.camera.turnTo(obj);
				}
			}
		} else {
			if (CBFMultitasker.runTries <= 2) {
				CBFMultitasker.runTries++;
				ctx.movement.step(taskArea.getRandomTile());
				Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						final Player p = Methods.this.ctx.players.local();
						return p.idle() && !p.inMotion();
					}
				});
			}
		}
	}

	public void chopAndBurn(final String treeType, final String locSelection,
			final Area taskArea) {
		if (ctx.widgets.widget(1184).component(11).text()
				.equalsIgnoreCase("Guard in tree")) {
			if (treeType.equalsIgnoreCase("Willow")
					&& locSelection.equalsIgnoreCase("Draynor Village")) {
				ctx.movement.step(ctx.players.local().tile().derive(2, 2));
				ctx.camera.angle('n');
			}
		}
		if (treeType.equalsIgnoreCase("Ivy")) {
			if (locSelection.equalsIgnoreCase("Varrock")) {
				if ((ctx.camera.yaw() < 134) || (ctx.camera.yaw() > 236)) {
					combineCamera(Random.nextInt(140, 230),
							Random.nextInt(9, 30));
					// ctx.camera.angle('s');
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() {
							final Camera c = ctx.camera;
							return (c.yaw() >= 134)
									|| (ctx.camera.yaw() <= 236);
						}
					}, 1000, 20);
				}
			} else {
				if (locSelection.equalsIgnoreCase("Falador")) {
					if ((ctx.camera.yaw() < 10) || (ctx.camera.yaw() > 80)) {
						combineCamera(Random.nextInt(14, 75),
								Random.nextInt(9, 30));
						// ctx.camera.angle('s');
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								final Camera c = ctx.camera;
								return (c.yaw() >= 10)
										|| (ctx.camera.yaw() <= 80);
							}
						}, 1000, 20);
					}
				}
			}
		}
		if (!treeType.equalsIgnoreCase("Ivy")) {
			if ((ctx.camera.pitch() < 15) || (ctx.camera.pitch() > 55)) {
				combineCamera(Random.nextInt(ctx.camera.yaw() - 5,
						ctx.camera.yaw() + 5), Random.nextInt(20, 50));
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						final Camera c = ctx.camera;
						return (c.pitch() >= 15) || (ctx.camera.pitch() <= 55);
					}
				}, 1000, 20);
			}
		} else {
			if ((ctx.camera.pitch() < 2) || (ctx.camera.pitch() > 18)) {
				combineCamera(Random.nextInt(ctx.camera.yaw() - 5,
						ctx.camera.yaw() + 5), Random.nextInt(4, 15));
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						final Camera c = ctx.camera;
						return (c.pitch() >= 2) || (ctx.camera.pitch() <= 18);
					}
				}, 1000, 20);
			}
		}
		GameObject objFinder = null;
		final int taskItemId = getTaskItemId(treeType);
		final int[] treeIds = getTaskTreeIds(treeType);
		if (treeType.equalsIgnoreCase("Normal")) {
			objFinder = ctx.objects.select(10).id(treeIds).within(taskArea)
					.nearest().limit(10).poll();
		} else {
			objFinder = ctx.objects.select(15).id(treeIds).within(taskArea)
					.nearest().limit(5).poll();
		}
		final GameObject obj = objFinder;
		if (obj.valid()) {
			if (obj.inViewport()) {
				final Widget fletchWidget1 = ctx.widgets.widget(1370);
				final Widget fletchWidget2 = ctx.widgets.widget(1371);
				if ((fletchWidget1.valid() && fletchWidget1.component(0)
						.visible())
						|| (fletchWidget2.valid() && fletchWidget2.component(0)
								.visible())) {
					final Point closePoint = fletchWidget2.component(20)
							.nextPoint();
					ctx.input.move(closePoint);
					Condition.wait(new Condition.Check() {

						@Override
						public boolean poll() {
							return ctx.input.getLocation().equals(closePoint);
						}
					});
					ctx.input.click(true);
					Condition.wait(new Condition.Check() {

						@Override
						public boolean poll() {
							return !(fletchWidget1.valid() && fletchWidget1
									.component(0).visible())
									|| (fletchWidget2.valid() && fletchWidget2
											.component(0).visible());
						}
					});
				} else {
					if (ctx.players.local().animation() == -1) {
						final Component chooseWidget = ctx.widgets.widget(1179)
								.component(30);
						if (chooseWidget.visible()) {
							if (ctx.backpack.select().id(taskItemId).count() > (28 - Random
									.nextInt(15, 25))) {
								final GameObject fireObj = ctx.objects
										.select(3).name("Fire").nearest()
										.poll();
								if (!fireObj.valid()) {
									ctx.input.send("1");
									Condition.wait(new Condition.Check() {
										@Override
										public boolean poll() {
											final Player p = Methods.this.ctx.players
													.local();
											final int backpackCount = Methods.this.ctx.backpack
													.select().count();
											return (Methods.this.ctx.backpack
													.select().count() <= (backpackCount - Random
													.nextInt(1, 4)))
													&& p.idle()
													&& !p.inMotion()
													&& (p.animation() == -1);
										}
									});
								} else {
									ctx.input.send("3");
									Condition.wait(new Condition.Check() {

										@Override
										public boolean poll() {
											final Player p = Methods.this.ctx.players
													.local();
											final int backpackCount = Methods.this.ctx.backpack
													.select().count();
											return (Methods.this.ctx.backpack
													.select().count() <= (backpackCount - Random
													.nextInt(1, 4)))
													&& p.idle()
													&& !p.inMotion()
													&& (p.animation() == -1);
										}
									});
								}
							} else {
								ctx.input.send("{VK_ESCAPE}");
							}
						} else {
							Condition.wait(new Condition.Check() {
								@Override
								public boolean poll() {
									final Player p = Methods.this.ctx.players
											.local();
									return p.idle();
								}
							});
							obj.interact(true, "Chop down",
									getTaskTreeString(treeType));
							CBFMultitasker.runTries = 0;
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									final Player p = Methods.this.ctx.players
											.local();
									return (p.animation() == -1)
											&& !p.inMotion() && p.idle();
								}
							}, 1200, 10);
						}
					} else {
						if (ctx.chat.queryContinue()) {
							ctx.chat.clickContinue(true);
						} else {
							if (ctx.combatBar.select().id(taskItemId).isEmpty()) {
								if (ctx.backpack.select().id(taskItemId)
										.count() > 0) {
									addItem(taskItemId);
								}
							} else {
								if (ctx.backpack.select().id(taskItemId)
										.count() > (28 - Random.nextInt(15, 25))) {
									final int slot = ctx.combatBar.select()
											.id(taskItemId).poll().slot();
									final String slotBind = ctx.combatBar
											.actionAt(slot).bind();
									final GameObject fireObj = ctx.objects
											.select(3).name("Fire").nearest()
											.poll();
									final Widget toolWidget = ctx.widgets
											.widget(1179);
									if (!fireObj.valid()) {
										if (toolWidget
												.component(39)
												.text()
												.equalsIgnoreCase(
														"Add to bonfire")) {
											ctx.input.send("1");
											Condition
													.wait(new Condition.Check() {

														@Override
														public boolean poll() {
															final Player p = Methods.this.ctx.players
																	.local();
															final int backpackCount = Methods.this.ctx.backpack
																	.select()
																	.count();
															return (Methods.this.ctx.backpack
																	.select()
																	.count() <= (backpackCount - Random
																	.nextInt(1,
																			4)))
																	&& p.idle()
																	&& !p.inMotion()
																	&& (p.animation() == -1);
														}
													});
										} else {
											ctx.input.send(slotBind);
										}
									} else {
										if (toolWidget
												.component(39)
												.text()
												.equalsIgnoreCase(
														"Add to bonfire")) {
											ctx.input.send("3");
											Condition
													.wait(new Condition.Check() {

														@Override
														public boolean poll() {
															final Player p = Methods.this.ctx.players
																	.local();
															final int backpackCount = Methods.this.ctx.backpack
																	.select()
																	.count();
															return (Methods.this.ctx.backpack
																	.select()
																	.count() <= (backpackCount - Random
																	.nextInt(1,
																			4)))
																	&& p.idle()
																	&& !p.inMotion()
																	&& (p.animation() == -1);
														}
													});
										} else {
											ctx.input.send(slotBind);
										}
									}
								}
							}

						}
					}
				}
			} else {
				if (treeType.equalsIgnoreCase("Ivy")) {
					ctx.movement.step(obj.tile());
				} else {
					ctx.camera.turnTo(obj);
				}
			}
		} else {
			if (ctx.backpack.select().id(taskItemId).count() > 0) {
				if (ctx.combatBar.select().id(taskItemId).isEmpty()) {
					addItem(taskItemId);
				} else {
					final int slot = ctx.combatBar.select().id(taskItemId)
							.poll().slot();
					final String slotBind = ctx.combatBar.actionAt(slot).bind();
					final GameObject fireObj = ctx.objects.select(3)
							.name("Fire").nearest().poll();
					final Widget toolWidget = ctx.widgets.widget(1179);
					if (!fireObj.valid()) {
						if (toolWidget.component(39).text()
								.equalsIgnoreCase("Add to bonfire")) {
							ctx.input.send("1");
							Condition.wait(new Condition.Check() {

								@Override
								public boolean poll() {
									final Player p = Methods.this.ctx.players
											.local();
									final int backpackCount = Methods.this.ctx.backpack
											.select().count();
									return (Methods.this.ctx.backpack.select()
											.count() <= (backpackCount - Random
											.nextInt(1, 4)))
											&& p.idle()
											&& !p.inMotion()
											&& (p.animation() == -1);
								}
							});
						} else {
							ctx.input.send(slotBind);
						}
					} else {
						if (toolWidget.component(39).text()
								.equalsIgnoreCase("Add to bonfire")) {
							ctx.input.send("3");
							Condition.wait(new Condition.Check() {

								@Override
								public boolean poll() {
									final Player p = Methods.this.ctx.players
											.local();
									final int backpackCount = Methods.this.ctx.backpack
											.select().count();
									return (Methods.this.ctx.backpack.select()
											.count() <= (backpackCount - Random
											.nextInt(1, 4)))
											&& p.idle()
											&& !p.inMotion()
											&& (p.animation() == -1);
								}
							});
						} else {
							ctx.input.send(slotBind);
						}
					}
				}
			} else {
				if (CBFMultitasker.runTries <= 2) {
					CBFMultitasker.runTries++;
					ctx.movement.step(taskArea.getRandomTile());
					Condition.wait(new Condition.Check() {
						@Override
						public boolean poll() {
							final Player p = Methods.this.ctx.players.local();
							return p.idle() && !p.inMotion();
						}
					});
				}
			}
		}
	}

	public void chopAndDrop(final String treeType, final String locSelection,
			final Area taskArea) {
		if (ctx.widgets.widget(1184).component(11).text()
				.equalsIgnoreCase("Guard in tree")) {
			if (treeType.equalsIgnoreCase("Willow")
					&& locSelection.equalsIgnoreCase("Draynor Village")) {
				ctx.movement.step(ctx.players.local().tile().derive(2, 2));
				ctx.camera.angle('n');
			}
		}
		if (treeType.equalsIgnoreCase("Ivy")) {
			if (locSelection.equalsIgnoreCase("Varrock")) {
				if ((ctx.camera.yaw() < 134) || (ctx.camera.yaw() > 236)) {
					combineCamera(Random.nextInt(140, 230),
							Random.nextInt(9, 30));
					// ctx.camera.angle('s');
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() {
							final Camera c = ctx.camera;
							return (c.yaw() >= 134)
									|| (ctx.camera.yaw() <= 236);
						}
					}, 1000, 20);
				}
			} else {
				if (locSelection.equalsIgnoreCase("Falador")) {
					if ((ctx.camera.yaw() < 10) || (ctx.camera.yaw() > 80)) {
						combineCamera(Random.nextInt(14, 75),
								Random.nextInt(9, 30));
						// ctx.camera.angle('s');
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								final Camera c = ctx.camera;
								return (c.yaw() >= 10)
										|| (ctx.camera.yaw() <= 80);
							}
						}, 1000, 20);
					}
				}
			}
		}
		if (!treeType.equalsIgnoreCase("Ivy")) {
			if ((ctx.camera.pitch() < 15) || (ctx.camera.pitch() > 55)) {
				combineCamera(Random.nextInt(ctx.camera.yaw() - 5,
						ctx.camera.yaw() + 5), Random.nextInt(20, 50));
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						final Camera c = ctx.camera;
						return (c.pitch() >= 15) || (ctx.camera.pitch() <= 55);
					}
				}, 1000, 20);
			}
		} else {
			if ((ctx.camera.pitch() < 2) || (ctx.camera.pitch() > 18)) {
				combineCamera(Random.nextInt(ctx.camera.yaw() - 5,
						ctx.camera.yaw() + 5), Random.nextInt(4, 15));
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						final Camera c = ctx.camera;
						return (c.pitch() >= 2) || (ctx.camera.pitch() <= 18);
					}
				}, 1000, 20);
			}
		}
		GameObject objFinder = null;
		final int taskItemId = getTaskItemId(treeType);
		final int[] ivyBounds = {-128, 128, -450, -950, -128, 128};
		final int[] treeIds = getTaskTreeIds(treeType);
		if (!treeType.equalsIgnoreCase("Ivy")) {
			if (treeType.equalsIgnoreCase("Normal")) {
				objFinder = ctx.objects.select(10).id(treeIds).within(taskArea)
						.nearest().limit(10).poll();
			} else {
				objFinder = ctx.objects.select(15).id(treeIds).within(taskArea)
						.nearest().limit(5).poll();
			}
		} else {
			objFinder = ctx.objects.select(15).id(treeIds)
					.each(Interactive.doSetBounds(ivyBounds)).within(taskArea)
					.nearest().limit(3).poll();
		}
		final GameObject obj = objFinder;
		if (obj.valid()) {
			if (obj.inViewport()) {
				if (ctx.players.local().animation() == -1) {
					final Component chooseWidget = ctx.widgets.widget(1179)
							.component(30);
					if (chooseWidget.visible()) {
						ctx.input.send("{VK_ESCAPE}");
					} else {
						if (treeType.equalsIgnoreCase("Ivy")) {
							obj.interact(true, "Chop",
									getTaskTreeString(treeType));
							CBFMultitasker.runTries = 0;
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									final Player p = Methods.this.ctx.players
											.local();
									return (p.animation() == -1)
											&& !p.inMotion() && p.idle();
								}
							}, 1200, 10);
						} else {
							obj.interact(true, "Chop down",
									getTaskTreeString(treeType));
							CBFMultitasker.runTries = 0;
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									final Player p = Methods.this.ctx.players
											.local();
									return (p.animation() == -1)
											&& !p.inMotion() && p.idle();
								}
							}, 1200, 10);
						}
					}
				} else {
					if (ctx.chat.queryContinue()) {
						ctx.chat.clickContinue(true);
					} else {
						if (ctx.combatBar.select().id(taskItemId).isEmpty()
								&& (ctx.backpack.select().id(taskItemId)
										.count() > 0)) {
							addItem(taskItemId);
						} else {
							if (ctx.backpack.select().id(taskItemId).count() > 0) {
								final int slot = ctx.combatBar.poll().slot();
								final int oldIndex = ctx.backpack.select()
										.id(taskItemId).count();
								ctx.combatBar.actionAt(slot).component()
										.interact("Drop");
								Condition.wait(new Callable<Boolean>() {
									@Override
									public Boolean call() {
										return (Methods.this.ctx.backpack
												.select().count() <= oldIndex)
												&& (Methods.this.ctx.players
														.local().animation() == -1);
									}
								}, 250, 4);
							}
						}
					}
				}
			} else {
				if (treeType.equalsIgnoreCase("Ivy")) {
					ctx.movement.step(obj.tile());
				} else {
					ctx.camera.turnTo(obj);
				}
			}
		} else {
			if (ctx.backpack.select().id(taskItemId).count() > 0) {
				if (ctx.combatBar.select().id(taskItemId).isEmpty()) {
					addItem(taskItemId);
				} else {
					final int slot = ctx.combatBar.poll().slot();
					final int oldIndex = ctx.backpack.select().id(taskItemId)
							.count();
					ctx.combatBar.actionAt(slot).component().interact("Drop");
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() {
							return (Methods.this.ctx.backpack.select().count() <= oldIndex)
									&& (Methods.this.ctx.players.local()
											.animation() == -1);
						}
					}, 250, 4);

				}
			} else {
				if (CBFMultitasker.runTries <= 2) {
					CBFMultitasker.runTries++;
					ctx.movement.step(taskArea.getRandomTile());
					Condition.wait(new Condition.Check() {
						@Override
						public boolean poll() {
							final Player p = Methods.this.ctx.players.local();
							return p.idle() && !p.inMotion();
						}
					});
				}
			}
		}
	}

	public boolean combineCamera(final int angle, final int pitch) {
		final Runnable setAngle = new Runnable() {
			@Override
			public void run() {
				Methods.this.ctx.camera.angle(angle);
			}
		};
		final Runnable setPitch = new Runnable() {
			@Override
			public void run() {
				Methods.this.ctx.camera.pitch(pitch);
			}
		};

		if (Random.nextInt(0, 100) < 50) {
			new Thread(setAngle).start();
			new Thread(setPitch).start();
		} else {
			new Thread(setPitch).start();
			new Thread(setAngle).start();
		}

		return Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return (Methods.this.ctx.camera.pitch() == pitch)
						&& (Methods.this.ctx.camera.yaw() == angle);
			}
		}, Random.nextInt(50, 200), Random.nextInt(4, 8));
	}

	public int[] concat(final int[] a, final int[] b) {
		final int aLen = a.length;
		final int bLen = b.length;
		final int[] c = new int[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	private void depositExtraAxeInInventory() {
		final int woodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		if (ctx.bank.opened()) {
			int bestAxeId = 0;
			if (woodcuttingLevel >= 41) {
				if (ctx.backpack.select().id(1355).count() > 0) {
					bestAxeId = 1355;
				}
				if (ctx.backpack.select().id(1357).count() > 0) {
					bestAxeId = 1357;
				}
				if (ctx.backpack.select().id(1359).count() > 0) {
					bestAxeId = 1359;
				}
			} else if (woodcuttingLevel >= 31) {
				if (ctx.backpack.select().id(1355).count() > 0) {
					bestAxeId = 1355;
				}
				if (ctx.backpack.select().id(1357).count() > 0) {
					bestAxeId = 1357;
				}
			} else if (woodcuttingLevel >= 21) {
				if (ctx.backpack.select().id(1355).count() > 0) {
					bestAxeId = 1355;
				}
			}
			if (bestAxeId != 0) {
				for (final Item item : ctx.backpack.select().id(1355, 1357,
						1359)) {
					if ((item.id() < bestAxeId)
							|| ((!ctx.backpack.select().id(bestAxeId).isEmpty()) && (returnToolbeltToItemId() >= bestAxeId))) {
						ctx.bank.deposit(item.id(), Amount.ALL);
					} else {
						if (ctx.backpack.select().id(item.id()).count() > 1) {
							ctx.bank.deposit(bestAxeId, (ctx.backpack.select()
									.id(bestAxeId).count() - 1));
						}
					}
				}
			}
		}
	}

	private void depositUnusableAxe() {
		final int woodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		if (ctx.bank.opened()) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				if (woodcuttingLevel < 21) {
					ctx.bank.deposit(1355, Amount.ALL);
				}
			}
			if (ctx.backpack.select().id(1357).count() > 0) {
				if (woodcuttingLevel < 31) {
					ctx.bank.deposit(1357, Amount.ALL);
				}
			}
			if (ctx.backpack.select().id(1359).count() > 0) {
				if (woodcuttingLevel < 41) {
					ctx.bank.deposit(1359, Amount.ALL);
				}
			}
		}
	}

	public void dropExtraItemIds(final String treeType) {
		final int[] extraIds = extraItemIds(treeType);
		final Widget destroyWidget = ctx.widgets.widget(1183);
		for (final Item i : ctx.backpack.select().id(extraIds)) {
			if (destroyWidget.component(16).visible()) {
				final Point catagoryMenuPoint = destroyWidget.component(16)
						.nextPoint();
				ctx.input.move(catagoryMenuPoint);
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						return Methods.this.ctx.input.getLocation() == catagoryMenuPoint;
					}
				}, 500, 4);
				ctx.input.click(true);
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						return destroyWidget.component(16).visible();
					}
				}, 500, 4);
			} else {
				if (ctx.chat.queryContinue()) {
					ctx.chat.clickContinue(true);
				} else {
					if ((i.id() >= 15540) && (i.id() <= 15545)) {
						i.interact("Destroy");
					} else {
						i.interact("Drop");
					}
				}
			}
		}
	}

	public void dropLogs(final String treeType) {
		final int taskItemId = getTaskItemId(treeType);
		if (ctx.chat.queryContinue()) {
			ctx.chat.clickContinue(true);
		} else {
			if (ctx.combatBar.select().id(taskItemId).isEmpty()) {
				addItem(taskItemId);
			} else {
				final int slot = ctx.combatBar.poll().slot();
				ctx.combatBar.actionAt(slot).component().interact("Drop");
			}
		}
	}

	public void equipBetterAxe() {
		if (!ctx.backpack.select().id(getBetterAxeId()).isEmpty()) {
			if (ctx.equipment.select().id(getBetterAxeId()).isEmpty()) {
				if (hasRequiredAttackLevel()) {
					for (final Item i : ctx.backpack.shuffle()) {
						i.interact("Wield");
					}
				}
			}
		}
	}

	public int[] extraItemIds(final String treeType) {
		// Norm: 1511, Oak: 1521, Willow: 1519, Maple: 1517, Yew: 1515
		// Strange Rock Ids: WC - 15542/43, Fletching - 15544/45, FM - 15540/41
		if (treeType.equalsIgnoreCase("Ivy")) {
			return new int[]{1511, 1521, 1519, 1517, 1515, 15540, 15541, 15542,
					15543, 15544, 15545};
		} else if (treeType.equalsIgnoreCase("Yew")) {
			return new int[]{1511, 1521, 1519, 1517, 15540, 15541, 15542,
					15543, 15544, 15545};
		} else if (treeType.equalsIgnoreCase("Maple")) {
			return new int[]{1511, 1521, 1519, 1515, 15540, 15541, 15542,
					15543, 15544, 15545};
		} else if (treeType.equalsIgnoreCase("Willow")) {
			return new int[]{1511, 1521, 1517, 1515, 15540, 15541, 15542,
					15543, 15544, 15545};
		} else if (treeType.equalsIgnoreCase("Oak")) {
			return new int[]{1511, 1519, 1517, 1515, 15540, 15541, 15542,
					15543, 15544, 15545};
		} else if (treeType.equalsIgnoreCase("Normal")) {
			return new int[]{1521, 1519, 1517, 1515, 15540, 15541, 15542,
					15543, 15544, 15545};
		} else {
			return new int[]{15540, 15541, 15542, 15543, 15544, 15545};
		}
	}

	public boolean fletchLogs(final String treeType, final String fletchOption) {
		final int taskItemId = getTaskItemId(treeType);
		if (ctx.chat.queryContinue()) {
			ctx.chat.clickContinue(true);
		} else {
			if (ctx.backpack.select().id(taskItemId).isEmpty()) {
				return false;
			} else {
				if (ctx.combatBar.select().id(taskItemId).isEmpty()
						&& (ctx.backpack.select().id(taskItemId).count() > 0)) {
					addItem(taskItemId);
				} else {
					final int slot = ctx.combatBar.select().id(taskItemId)
							.poll().slot();
					final String slotBind = ctx.combatBar.actionAt(slot).bind();
					final Widget progressWidget = ctx.widgets.widget(1251);
					if (progressWidget.component(0).valid()
							&& progressWidget.component(0).visible()) {
						Condition.wait(new Condition.Check() {

							@Override
							public boolean poll() {
								return !progressWidget.component(0).visible();
							}
						});
					} else {
						final Widget toolWidget = ctx.widgets.widget(1179);
						final Widget fletchWidget1 = ctx.widgets.widget(1370);
						final Widget fletchWidget2 = ctx.widgets.widget(1371);
						if (toolWidget.component(0).valid()
								&& toolWidget.component(0).text()
										.contains("logs")) {
							ctx.input.send("2");
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return fletchWidget1.valid();
								}
							}, 1000, 2);
						} else {
							if (fletchWidget1.component(38).text()
									.equalsIgnoreCase("Fletch")) {
								if (fletchWidget2.component(51).component(0)
										.text().contains(treeType)) {
									if (fletchWidget1
											.component(56)
											.text()
											.toLowerCase()
											.contains(
													fletchOption.toLowerCase())) {
										final Point fletchPoint = fletchWidget1
												.component(38).nextPoint();
										ctx.input.move(fletchPoint);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return Methods.this.ctx.input
														.getLocation() == fletchPoint;
											}
										}, 500, 4);
										ctx.input.click(true);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return (Methods.this.ctx.players
														.local().animation() == -1)
														&& !Methods.this.ctx.widgets
																.widget(1251)
																.valid()
														&& !Methods.this.ctx.widgets
																.widget(1251)
																.component(0)
																.visible();
											}
										}, 4000, 2);
									} else {
										// 1371 - 44 [ 4 ]
										final Point selectionMenuPoint = fletchWidget2
												.component(44)
												.component(
														getOptionWidget(
																fletchOption,
																treeType))
												.nextPoint();
										ctx.input.move(selectionMenuPoint);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return Methods.this.ctx.input
														.getLocation() == selectionMenuPoint;
											}
										}, 500, 4);
										ctx.input.click(true);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return fletchWidget1
														.component(56)
														.text()
														.toLowerCase()
														.contains(
																fletchOption
																		.toLowerCase());
											}
										}, 500, 4);
									}
								} else {
									// normal = 62 . 0
									// archery = 62 . 1
									// oak2, willow3,
									// maple5, yew7, magic8,
									// elder10
									// category comp. to
									// click = 49
									if (fletchWidget2.component(62).visible()) {
										final Point catagoryMenuPoint = fletchWidget2
												.component(62)
												.component(
														getOptionCategory(treeType))
												.nextPoint();
										ctx.input.move(catagoryMenuPoint);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return Methods.this.ctx.input
														.getLocation() == catagoryMenuPoint;
											}
										}, 500, 4);
										ctx.input.click(true);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return fletchWidget2
														.component(51)
														.component(0).text()
														.contains(treeType);
											}
										}, 500, 4);
									} else {
										final Point catagoryMenuPoint = fletchWidget2
												.component(49).nextPoint();
										ctx.input.move(catagoryMenuPoint);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return Methods.this.ctx.input
														.getLocation() == catagoryMenuPoint;
											}
										}, 500, 4);
										ctx.input.click(true);
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return fletchWidget2.component(
														62).visible();
											}
										}, 500, 4);
									}
								}
							} else {
								ctx.input.send(slotBind);
							}
						}
					}
				}
			}
		}
		return true;
	}

	public boolean fletchLogsMode(final String typeSelection) {
		return typeSelection.equalsIgnoreCase("Fletch");
	}

	public TilePath getBankToTreesPath(final String logType,
			final String locString) {
		return new TilePath(ctx, PathTiles.TREE_TO_BANK.get(logType + " "
				+ locString)).reverse();
	}

	private int getBetterAxeId() {
		final int woodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		if (woodcuttingLevel >= 61) {
			return 1359;
		} else if (woodcuttingLevel >= 41) {
			return 1359;
		} else if (woodcuttingLevel >= 31) {
			return 1357;
		} else if (woodcuttingLevel >= 21) {
			return 1355;
		} else if (woodcuttingLevel >= 6) {
			return 1353;
		}
		return -1;
	}

	public int getFletchTaskItemId(final String treeType,
			final String optionSelection) {
		if (optionSelection.equalsIgnoreCase("Shortbow")) {
			if (treeType.equalsIgnoreCase("Normal")) {
				return 50;
			} else if (treeType.equalsIgnoreCase("Oak")) {
				return 54;
			} else if (treeType.equalsIgnoreCase("Willow")) {
				return 60;
			} else if (treeType.equalsIgnoreCase("Maple")) {
				return 64;
			} else if (treeType.equalsIgnoreCase("Yew")) {
				return 68;
			}
		} else if (optionSelection.equalsIgnoreCase("Shieldbow")) {
			if (treeType.equalsIgnoreCase("Normal")) {
				return 48;
			} else if (treeType.equalsIgnoreCase("Oak")) {
				return 56;
			} else if (treeType.equalsIgnoreCase("Willow")) {
				return 58;
			} else if (treeType.equalsIgnoreCase("Maple")) {
				return 62;
			} else if (treeType.equalsIgnoreCase("Yew")) {
				return 66;
			}
		} else if (optionSelection.equalsIgnoreCase("Stock")) {
			if (treeType.equalsIgnoreCase("Normal")) {
				return 9440;
			} else if (treeType.equalsIgnoreCase("Oak")) {
				return 9442;
			} else if (treeType.equalsIgnoreCase("Willow")) {
				return 9444;
			} else if (treeType.equalsIgnoreCase("Maple")) {
				return 9448;
			} else if (treeType.equalsIgnoreCase("Yew")) {
				return 9452;
			}
		} else if (optionSelection.equalsIgnoreCase("Arrow Shaft")) {
			return 52;
		}
		return -1;
	}

	public TilePath getLodestoneToBankPath(final String logType,
			final String locString) {
		return new TilePath(ctx, PathTiles.LODESTONE_TO_BANK.get(logType + " "
				+ locString));
	}

	public TilePath getLodestoneToTreesPath(final String logType,
			final String locString) {
		return new TilePath(ctx, PathTiles.LODESTONE_TO_TREE.get(logType + " "
				+ locString));
	}

	public boolean getNotes(final String locString) {
		final Component chooseWidget = ctx.widgets.widget(1179).component(30);
		if (chooseWidget.visible()) {
			ctx.input.send("{VK_ESCAPE}");
		} else {
			if (locString.equalsIgnoreCase("Port Sarim")) {
				return true;
			} else {
				if (!ctx.bank.opened()) {
					final Npc bankers = ctx.npcs.select()
							.id(Constants.BANK_NPCS).limit(10).nearest().poll();
					if (!bankers.valid()) {
						if (ctx.bank.inViewport()) {
							ctx.bank.open();
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return Methods.this.ctx.bank.opened();
								}
							}, 2000, 5);
						} else {
							ctx.camera.turnTo(ctx.bank.nearest().tile());
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return Methods.this.ctx.bank.inViewport();
								}
							}, 1000, 5);
						}
					} else {
						bankers.interact("Bank");
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								return Methods.this.ctx.bank.opened()
										|| Methods.this.ctx.players.local()
												.inMotion();
							}
						}, 2000, 5);
					}
				} else {
					final int[] allItemIds = concat(allLogIds(),
							allUnstrungBowIds());
					if (ctx.backpack.select().id(allItemIds).count() > 0) {
						for (final int allItemId : allItemIds) {
							if (ctx.backpack.select().id(allItemId).count() > 0) {
								ctx.bank.deposit(
										ctx.backpack.select().id(allItemId)
												.poll().id(), Amount.ALL);
							}
						}
					} else {
						if (ctx.bank.select().id(allItemIds).count() <= 0) {
							return true; // this means we got everything from
							// the bank
						} else {
							if (!ctx.bank.withdrawMode()) {
								ctx.bank.withdrawMode(true);
							} else {
								for (final int allItemId : allItemIds) {
									if (ctx.bank.select().id(allItemId).count() > 0) {
										ctx.bank.withdraw(allItemId, Amount.ALL);
									}
								}
							}
						}
					}

					if (ctx.bank.select().id(allItemIds).count() != 0) {
						if (ctx.backpack.select().id(allItemIds).count() >= 1) {
							if (!ctx.bank.withdrawMode()) {
								ctx.bank.withdrawMode(true);
							} else {
								for (final int allItemId : allNoteIds()) {
									if (ctx.bank.select().id(allItemId).count() > 0) {
										ctx.bank.withdraw(allItemId, Amount.ALL);
									}
								}
							}
						} else {
							ctx.bank.deposit(
									ctx.backpack.select().id(allItemIds).poll()
											.id(), Amount.ALL);
						}
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int getOptionCategory(final String treeType) {
		if (treeType.contains("Normal")) {
			return 0;
		} else if (treeType.contains("Oak")) {
			return 2;
		} else if (treeType.contains("Willow")) {
			return 3;
		} else if (treeType.contains("Maple")) {
			return 5;
		} else if (treeType.contains("Yew")) {
			return 7;
		} else if (treeType.contains("Magic")) {
			return 8;
		} else if (treeType.contains("Elder")) {
			return 10;
		}
		return -1;
	}

	public int getOptionWidget(String option, final String treeType) {
		option = option.toLowerCase();
		if (!treeType.equalsIgnoreCase("Magic")) {
			if (option.contains("arrow shaft")) {
				return 0;
			} else if (option.contains("shortbow")) {
				return 4;
			} else if (option.contains("stock")) {
				return 8;
			} else if (option.contains("shieldbow")) {
				return 12;
			}
		} else {
			if (option.contains("arrow shaft")) {
				return 0;
			} else if (option.contains("shortbow")) {
				return 4;
			} else if (option.contains("stock")) {
				return 12;
			} else if (option.contains("shieldbow")) {
				return 8;
			}
		}
		return -1;
	}

	public Area getOverallTaskArea(final String locString) {
		return Areas.OVERALL.get(locString);
	}

	public int getRandomInventInt() {
		return randomInventInt;
	}

	public Area getTaskBankArea(final String logType, final String locString) {
		return Areas.BANK_AREA.get(logType + " " + locString);
	}

	public String getTaskDropAction(final String type) {
		if (type.contains("Chop")) {
			if (type.contains("Burn")) {
				return "Light";
			} else if (type.contains("Fletch")) {
				return "Craft";
			} else {
				return "Drop";
			}
		}
		return "Drop";
	}

	public int getTaskItemId(final String logType) {
		if (logType.equalsIgnoreCase("Ivy")) {
			return 0;
		} else if (logType.equalsIgnoreCase("Yew")) {
			return 1515;
		} else if (logType.equalsIgnoreCase("Maple")) {
			return 1517;
		} else if (logType.equalsIgnoreCase("Willow")) {
			return 1519;
		} else if (logType.equalsIgnoreCase("Oak")) {
			return 1521;
		} else if (logType.equalsIgnoreCase("Normal")) {
			return 1511;
		}
		return -1;
	}

	public Area getTaskSecdArea(final String option, final String logType,
			final String locString) {
		return Areas.SECD_AREA.get(option + " " + logType + " " + locString);
	}

	public Area getTaskTreeArea(final String logType, final String locString) {
		return Areas.TREE_AREA.get(logType + " " + locString);
	}

	public int[] getTaskTreeIds(final String logType) {
		if (logType.equalsIgnoreCase("Ivy")) {
			return new int[]{46318, 46322, 46324, 86561, 86562, 86563, 86564};
		} else if (logType.equalsIgnoreCase("Yew")) {
			return new int[]{38755};
		} else if (logType.equalsIgnoreCase("Maple")) {
			return new int[]{51843, 51845};
		} else if (logType.equalsIgnoreCase("Willow")) {
			return new int[]{38616, 38627,};
		} else if (logType.equalsIgnoreCase("Oak")) {
			return new int[]{38731, 38732};
		} else if (logType.equalsIgnoreCase("Normal")) {
			return new int[]{38760, 38762, 38782, 38783, 38784, 38786, 38788};
		}
		return new int[]{};
	}

	public String getTaskTreeString(final String logType) {
		if (logType.equalsIgnoreCase("Normal")) {
			return "Tree";
		} else if (logType.equalsIgnoreCase("Maple")) {
			return "Maple Tree";
		} else {
			return logType;
		}
	}

	public TilePath getTreesToBankPath(final String logType,
			final String locString) {
		return new TilePath(ctx, PathTiles.TREE_TO_BANK.get(logType + " "
				+ locString));
	}

	public int getZoomLevel(final String treeType) {
		if (treeType.equalsIgnoreCase("Regular")) {
			return 10;
		} else if (treeType.equalsIgnoreCase("Oak")) {
			return 6;
		} else if (treeType.equalsIgnoreCase("Willow")) {
			return 8;
		} else if (treeType.equalsIgnoreCase("Maple")) {
			return 8;
		} else if (treeType.equalsIgnoreCase("Yew")) {
			return 7;
		} else if (treeType.equalsIgnoreCase("Ivy")) {
			return 4;
		} else {
			return 5;
		}
	}

	public boolean hasBetterAxeInInventory() {
		if (ctx.backpack.select().id(getBetterAxeId()).count() > 0) {
			if (returnToolbeltToItemId() != getBetterAxeId()) {
				return true;
			}
		}
		return false;
	}

	private boolean hasExtraAxeInInventory() {
		final int woodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		int bestAxeId = 0;
		boolean hasExtraAxe = false;
		if (woodcuttingLevel >= 41) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				bestAxeId = 1355;
			}
			if (ctx.backpack.select().id(1357).count() > 0) {
				if (bestAxeId == 1355) {
					hasExtraAxe = true;
				}
				bestAxeId = 1357;
			}
			if (ctx.backpack.select().id(1359).count() > 0) {
				if ((bestAxeId == 1355) || (bestAxeId == 1357)) {
					hasExtraAxe = true;
				}
				bestAxeId = 1359;
			}
		} else if (woodcuttingLevel >= 31) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				bestAxeId = 1355;
			}
			if (ctx.backpack.select().id(1357).count() > 0) {
				if (bestAxeId == 1355) {
					hasExtraAxe = true;
				}
				bestAxeId = 1357;
			}
		} else if (woodcuttingLevel >= 21) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				bestAxeId = 1355;
			}
		}
		if (bestAxeId != 0) {
			if ((ctx.backpack.select().id(bestAxeId).count() > 1)
					|| ((!ctx.backpack.select().id(bestAxeId).isEmpty()) && (returnToolbeltToItemId() >= bestAxeId))) {
				hasExtraAxe = true;
			}
		}
		return hasExtraAxe;
	}

	public boolean hasExtraFletchItems(final String treeType,
			final String optionSelection) {
		if ((optionSelection != null)
				&& (optionSelection.contains("Arrow")
						|| optionSelection.contains("Short")
						|| optionSelection.contains("Stock") || optionSelection
							.contains("Shield"))) {
			if (ctx.backpack.select().id(allUnstrungBowIds()).count() == ctx.backpack
					.select()
					.id(getFletchTaskItemId(treeType, optionSelection)).count()) {
				return false;
			}
		} else {
			if (ctx.backpack.select().id(allUnstrungBowIds()).count() == 0) {
				return false;
			}
		}
		return true;
	}

	public boolean hasExtraItems(final String treeType) {
		if (ctx.backpack.select().id(extraItemIds(treeType)).count() > 0) {
			return true;
		}
		return false;
	}

	private boolean hasRequiredAttackLevel() {
		final int woodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		final int strLevel = ctx.skills.level(Constants.SKILLS_STRENGTH);
		if (woodcuttingLevel >= 41) {
			if (strLevel >= 50) {
				return true;
			}
		} else if (woodcuttingLevel >= 31) {
			if (strLevel >= 40) {
				return true;
			}
		} else if (woodcuttingLevel >= 21) {
			if (strLevel >= 30) {
				return true;
			}
		} else if (woodcuttingLevel >= 6) {
			if (strLevel >= 20) {
				return true;
			}
		}
		return false;
	}

	private boolean hasUnusableAxe() {
		final int woodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		if (ctx.backpack.select().id(1355).count() > 0) {
			if (woodcuttingLevel < 21) {
				return true;
			}
		}
		if (ctx.backpack.select().id(1357).count() > 0) {
			if (woodcuttingLevel < 31) {
				return true;
			}
		}
		if (ctx.backpack.select().id(1359).count() > 0) {
			if (woodcuttingLevel < 41) {
				return true;
			}
		}
		return false;
	}

	public boolean mouseCameraMove(final Locatable locatable, final Point point) {
		final Runnable moveCamera = new Runnable() {
			@Override
			public void run() {
				final int random = Random.nextInt(0, 99);
				while (locatable.tile().matrix(Methods.this.ctx).centerPoint().x == -1) {
					if (random < 49) {
						Methods.this.ctx.input.send("{VK_LEFT}");
					} else {
						Methods.this.ctx.input.send("{VK_RIGHT}");
					}
				}
			}
		};
		final Runnable moveMouse = new Runnable() {
			@Override
			public void run() {
				while (Methods.this.ctx.input.getLocation() != point) {
					Methods.this.ctx.input.move(point);
				}
			}
		};

		if (Random.nextInt(0, 99) < 49) {
			new Thread(moveCamera).start();
			new Thread(moveMouse).start();
		} else {
			new Thread(moveMouse).start();
			new Thread(moveCamera).start();
		}

		return Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return locatable.tile().matrix(Methods.this.ctx).inViewport()
						&& (Methods.this.ctx.input.getLocation() == point);
			}
		}, Random.nextInt(1500, 3000), Random.nextInt(2, 8));
	}

	private int returnToolbeltToItemId() {
		final int toolbeltAxe = ctx.varpbits.varpbit(1102);
		int itemId = 0;
		if (toolbeltAxe == 84934639) {
			itemId = 1359;
		}
		if (toolbeltAxe == 68157423) {
			itemId = 1357;
		}
		if (toolbeltAxe == 51380207) {
			itemId = 1355;
		}
		return itemId;
	}

	public boolean runAway(final Tile runawayTile) {
		if (ctx.bank.opened()) {
			ctx.bank.close();
		}
		final Widget bankWidget = ctx.widgets.widget(11);
		if (bankWidget.valid()) {
			ctx.input.click(bankWidget.component(41).component(1).nextPoint(),
					true);
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					return !bankWidget.valid();
				}
			}, 1000, 20);
		}
		if (!ctx.players.local().inCombat()) {
			return true;
		}
		if (ctx.players.local().tile().equals(runawayTile)) {
			return true;
		} else {
			ctx.movement.step(runawayTile);
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					final Player p = Methods.this.ctx.players.local();
					return p.idle() && !p.inMotion();
				}
			});
		}
		return false;
	}

	public void runToBank(final String locString, final Area area,
			final TilePath treesToBankPath, final TilePath lodestoneToBankPath) {
		if (ctx.movement.distance(area.getCentralTile()) < 20) {
			if (locString.equalsIgnoreCase("Port Sarim")) {
				final GameObject depositBox = ctx.objects.select().id(36788)
						.nearest().poll();
				final Widget bankWidget2 = ctx.widgets.widget(11);
				if (depositBox.valid()) {
					if (!bankWidget2.valid()) {
						if (depositBox.inViewport()) {
							depositBox.interact(true, "Deposit");
							Condition.wait(new Condition.Check() {

								@Override
								public boolean poll() {
									return bankWidget2.valid();
								}
							});
						} else {
							ctx.camera.turnTo(depositBox);
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return depositBox.inViewport();
								}
							}, 1000, 5);
						}
					}
				}
			} else {
				final Npc bankers = ctx.npcs.select().id(Constants.BANK_NPCS)
						.limit(10).nearest().poll();
				if (!ctx.bank.opened()) {
					if (bankers.valid()) {
						if (bankers.inViewport()) {
							bankers.interact("Bank");
							Condition.wait(new Condition.Check() {

								@Override
								public boolean poll() {
									return ctx.bank.opened();
								}
							});
						} else {
							ctx.camera.turnTo(bankers);
						}
					}
				}
			}
		}
		if (ctx.players.local().animation() != -1) {
			runAway(ctx.players.local().tile()
					.derive(Random.nextInt(-5, 5), Random.nextInt(-5, 5)));
		} else {
			TilePath lodestoneToBank = null;
			if (lodestoneToBankPath != null) {
				lodestoneToBank = ctx.movement.newTilePath(lodestoneToBankPath
						.array());
			}
			if ((lodestoneToBank != null) && (lodestoneToBank.next() != null)) {
				if (!ctx.players.local().inMotion()
						|| ((!Double.isInfinite(ctx.movement.destination()
								.distanceTo(ctx.players.local()))) && (ctx.movement
								.destination().distanceTo(ctx.players.local()) < Random
								.nextInt(7, 20)))) {
					ctx.movement.step(lodestoneToBank.next());
				}
			} else {
				TilePath treesToBank = null;
				if (treesToBankPath != null) {
					treesToBank = ctx.movement.newTilePath(treesToBankPath
							.array());
				}
				if ((treesToBank != null) && (treesToBank.next() != null)) {
					if (!ctx.players.local().inMotion()
							|| ((!Double.isInfinite(ctx.movement.destination()
									.distanceTo(ctx.players.local()))) && (ctx.movement
									.destination().distanceTo(
											ctx.players.local()) < Random
									.nextInt(7, 20)))) {
						ctx.movement.step(treesToBank.next());
					}
				} else {
					LocalPath pathToBank = null;
					if (area.getClosestTo(ctx.players.local()) != null) {
						pathToBank = ctx.movement.findPath(area
								.getClosestTo(ctx.players.local()));
					}
					if ((pathToBank != null)) {
						if (!ctx.players.local().inMotion()
								|| ((!Double.isInfinite(ctx.movement
										.destination().distanceTo(
												ctx.players.local()))) && (ctx.movement
										.destination().distanceTo(
												ctx.players.local()) < Random
										.nextInt(7, 20)))) {
							pathToBank.traverse();
						}
					} else {
						if ((ctx.players.local().animation() == -1)
								&& !ctx.players.local().inMotion()) {
							teleportToKnownLocation(locString);
						}
					}
				}
			}
		}
	}

	public void runToTreeArea(final String locString, final Area treeArea,
			final TilePath bankToTreesPath, final TilePath lodestoneToTreesPath) {
		if (ctx.bank.opened()) {
			ctx.bank.close();
		}
		final Widget bankWidget = ctx.widgets.widget(11);
		if (bankWidget.valid()) {
			ctx.input.click(bankWidget.component(41).component(1).nextPoint(),
					true);
			Condition.wait(new Condition.Check() {

				@Override
				public boolean poll() {
					return !bankWidget.valid();
				}
			});
		}
		if (ctx.players.local().animation() != -1) {
			runAway(ctx.players.local().tile()
					.derive(Random.nextInt(-5, 5), Random.nextInt(-5, 5)));
		} else {
			TilePath lodestoneToTrees = null;
			if (lodestoneToTreesPath != null) {
				lodestoneToTrees = ctx.movement
						.newTilePath(lodestoneToTreesPath.array());
			}
			if ((lodestoneToTrees != null) && (lodestoneToTrees.next() != null)) {
				if (!ctx.players.local().inMotion()
						|| ((!Double.isInfinite(ctx.movement.destination()
								.distanceTo(ctx.players.local()))) && (ctx.movement
								.destination().distanceTo(ctx.players.local()) < Random
								.nextInt(7, 20)))) {
					ctx.movement.step(lodestoneToTrees.next());
				}
			} else {
				TilePath bankToTrees = null;
				if (bankToTreesPath != null) {
					bankToTrees = ctx.movement.newTilePath(bankToTreesPath
							.array());
				}
				if ((bankToTrees != null) && (bankToTrees.next() != null)) {
					if (!ctx.players.local().inMotion()
							|| ((!Double.isInfinite(ctx.movement.destination()
									.distanceTo(ctx.players.local()))) && (ctx.movement
									.destination().distanceTo(
											ctx.players.local()) < Random
									.nextInt(7, 20)))) {
						ctx.movement.step(bankToTrees.next());
					}
				} else {
					LocalPath pathToTree = null;
					if (treeArea.getClosestTo(ctx.players.local()) != null) {
						pathToTree = ctx.movement.findPath(treeArea
								.getClosestTo(ctx.players.local()));
					}
					if ((pathToTree != null)) {
						if (!ctx.players.local().inMotion()
								|| ((!Double.isInfinite(ctx.movement
										.destination().distanceTo(
												ctx.players.local()))) && (ctx.movement
										.destination().distanceTo(
												ctx.players.local()) < Random
										.nextInt(7, 20)))) {
							pathToTree.traverse();
						}
					} else {
						if ((ctx.players.local().animation() == -1)
								&& !ctx.players.local().inMotion()) {
							teleportToKnownLocation(locString);
						}
					}
				}
			}
		}
	}

	public boolean setZoomLevel(final int zoomLevel) {
		for (int i = 0; i < 10; i++) {
			ctx.input.send("{VK_PAGE_UP}");
		}
		for (int i = 0; i < zoomLevel; i++) {
			ctx.input.send("{VK_PAGE_DOWN}");
		}
		return true;
	}

	public int shaftsPerLog(final String treeType) {
		if (treeType.equalsIgnoreCase("Normal")) {
			return 15;
		} else if (treeType.equalsIgnoreCase("Oak")) {
			return 20;
		} else if (treeType.equalsIgnoreCase("Willow")) {
			return 25;
		} else if (treeType.equalsIgnoreCase("Maple")) {
			return 30;
		} else if (treeType.equalsIgnoreCase("Yew")) {
			return 35;
		}
		return -1;
	}

	public boolean teleportToKnownLocation(final String locString) {
		if (!CBFMultitasker.useLodestone) {
			ctx.controller.stop();
		}
		if (ctx.bank.opened()) {
			ctx.bank.close();
		}
		final Widget bankWidget = ctx.widgets.widget(11);
		if (bankWidget.valid()) {
			ctx.input.click(bankWidget.component(41).component(1).nextPoint(),
					true);
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					return !bankWidget.valid();
				}
			}, 1000, 20);
		}
		if (locString.contains("Draynor")) {
			if (Lodestone.DRAYNOR.canUse(ctx)) {
				return Lodestone.DRAYNOR.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Port Sarim")) {
			if (Lodestone.PORT_SARIM.canUse(ctx)) {
				return Lodestone.PORT_SARIM.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Varrock")) {
			if (Lodestone.VARROCK.canUse(ctx)) {
				return Lodestone.VARROCK.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Falador")) {
			if (Lodestone.FALADOR.canUse(ctx)) {
				return Lodestone.FALADOR.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Edgeville")) {
			if (Lodestone.EDGEVILLE.canUse(ctx)) {
				return Lodestone.EDGEVILLE.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Seers' Village")
				|| locString.contains("McGrubor's Wood")
				|| locString.contains("Sorcerer's")) {
			if (Lodestone.SEERS_VILLAGE.canUse(ctx)) {
				return Lodestone.SEERS_VILLAGE.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Catherby")) {
			if (Lodestone.CATHERBY.canUse(ctx)) {
				return Lodestone.CATHERBY.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Yanille")
				|| locString.contains("Castle Wars")) {
			if (Lodestone.YANILlE.canUse(ctx)) {
				return Lodestone.YANILlE.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Neitiznot")) {
			if (Lodestone.FREMENIK_PROVINCE.canUse(ctx)) {
				return Lodestone.FREMENIK_PROVINCE.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Eagles' Peek")
				|| locString.contains("Tree Gnome Stronghold")) {
			if (Lodestone.EAGLES_PEEK.canUse(ctx)) {
				return Lodestone.EAGLES_PEEK.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Tirannwn")) {
			if (Lodestone.TIRANNWN.canUse(ctx)) {
				return Lodestone.TIRANNWN.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		} else if (locString.contains("Prifddinas")) {
			if (Lodestone.PRIFDDINAS.canUse(ctx)) {
				return Lodestone.PRIFDDINAS.teleport(ctx);
			} else {
				CBFMultitasker.removeLeadingTask();
			}
		}
		return false;
	}

	public boolean tileContainsObject() {
		final Tile ptile = ctx.players.local().tile();
		final GameObject closestObj = ctx.objects.select(1).poll();
		if (closestObj.tile().equals(ptile)) {
			return true;
		}
		return false;
	}

	public boolean tileContainsObject(final GameObject obj) {
		final Tile ptile = ctx.players.local().tile();
		if (obj.tile().equals(ctx.objects.at(ptile).poll())) {
			return true;
		}
		return false;
	}

	public boolean tileContainsObject(final Tile tile) {
		final GameObject closestObj = ctx.objects.at(tile).poll();
		if (closestObj.tile().equals(tile)) {
			return true;
		}
		return false;
	}

	public boolean tileContainsObject(final Tile tile, final GameObject obj) {
		if (obj.equals(ctx.objects.at(tile).poll())) {
			return true;
		}
		return false;
	}

	public boolean tileHasFire() {
		final GameObject obj = ctx.objects.select().id(70757, 70761).nearest()
				.poll();
		if (ctx.movement.distance(obj) == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void withdrawBestUsableAxe() {
		final int woodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		if (ctx.bank.opened()) {
			if (woodcuttingLevel >= 41) {
				if (ctx.bank.select().id(1359).count() > 0) {
					if (returnToolbeltToItemId() != 1359) {
						if (ctx.equipment.select().id(1359).count() == 0) {
							if (ctx.backpack.select().id(1359).count() == 0) {
								ctx.bank.withdraw(1359, Amount.ONE);
							}
						}
					}
				} else if (ctx.bank.select().id(1357).count() > 0) {
					if (returnToolbeltToItemId() < 1357) {
						if (ctx.equipment.select().id(1357).count() == 0) {
							if (ctx.backpack.select().id(1357).count() == 0) {
								ctx.bank.withdraw(1357, Amount.ONE);
							}
						}
					}
				} else if (ctx.bank.select().id(1355).count() > 0) {
					if (returnToolbeltToItemId() < 1355) {
						if (ctx.equipment.select().id(1355).count() == 0) {
							if (ctx.backpack.select().id(1355).count() == 0) {
								ctx.bank.withdraw(1355, Amount.ONE);
							}
						}
					}
				}
			} else if (woodcuttingLevel >= 31) {
				if (ctx.bank.select().id(1357).count() > 0) {
					if (returnToolbeltToItemId() < 1357) {
						if (ctx.equipment.select().id(1357).count() == 0) {
							if (ctx.backpack.select().id(1357).count() == 0) {
								ctx.bank.withdraw(1357, Amount.ONE);
							}
						}
					}
				} else if (ctx.bank.select().id(1355).count() > 0) {
					if (returnToolbeltToItemId() < 1355) {
						if (ctx.equipment.select().id(1355).count() == 0) {
							if (ctx.backpack.select().id(1355).count() == 0) {
								ctx.bank.withdraw(1355, Amount.ONE);
							}
						}
					}
				}
			} else if (woodcuttingLevel >= 21) {
				if (ctx.bank.select().id(1355).count() > 0) {
					if (returnToolbeltToItemId() < 1355) {
						if (ctx.equipment.select().id(1355).count() == 0) {
							if (ctx.backpack.select().id(1355).count() == 0) {
								ctx.bank.withdraw(1355, Amount.ONE);
							}
						}
					}
				}
			}
		}
	}

}