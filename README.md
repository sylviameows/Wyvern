> [!WARNING]
> This project is actively under development and not suitable for use.

# WyvernAPI
> A modding API for TrainMurderMystery (aka Harpy Express or TMM)

> [!WARNING]
> This project is actively under development and not suitable for use yet. <br>
> Feel free to preview and contribute, but i do not have the api available on a repository yet for modding.


This API sets out to simplify the modding process of TMM, making it easier to make custom roles, tasks, shops/shop items, and win conditions.

Wyvern additionally acts as a TMM utility mod, giving server owners more control over how the game plays and its balanced while also providing useful quality of life features for players. 

_**This mod must be installed server and client side to work!**_

## Creating a Task:
Creating a task is really simple with Wyvern with two basic types of tasks, `Tasks` and `TimedTasks`.
Tasks are the standard implementation, usually you'll just create a "fulfill" function and call that when it's complete (ex. Eat/Drink tasks from TMM). However, Timed Tasks include a default fulfillment quota of ticking time to 0 (ex. Sleep/Outside tasks from TMM). 

Once you've created your task, simply run: 
- `WyvernAPI.registerTask(id, nbt -> new Task());` for standard tasks with no special data attached.
- `WyvernAPI.registerTask(id, nbt -> new TimerTask(nbt.getInt("time")), () -> new TimerTask(5 * 20))` for timer tasks or tasks with stored data. The first function should load the data from nbt, the second should create a task with the default values.

For examples, look at the default tasks inside Wyvern.

## Translations Guide:
> [!WARNING]
> This guide is out of date and wont be updated until I start making proper docs.
Wyvern uses custom translations to make things more extensible, heres how they're set up.

**Roles** require 4 translations.
```json
{
  "{mod_id}.role.{role_id}.name": "{name}",
  "{mod_id}.role.{role_id}.team": "{name}s",
  "{mod_id}.role.{role_id}.goal": "{describe the role}",
}
```
The mod uses `{...}.name` as the roles name, and `{...}.team` when displayed in the endgame screen. The `{...}.goal` should describe their objective/role.
The `{...}.win` is a little different, this is what will show up when they win, it will likely be "Passengers Win!" or "Killers Win!" depending on the side, however if the role alignment is not with either, it will likely be the role name.

Some addons may wish to create **custom win statuses**, either for custom roles that act on their own, or some custom functionality that causes a role to win.
This can be set up in the following way.
```json
{
  "{mod_id}.result.{alignment}.{reason}": "{why they won}" 
}
```

### Examples
**Jester** a jester role may be written in the following way.
```json
{
  "wyvern_roles.role.jester.name": "Jester",
  "wyvern_roles.role.jester.announce": "Jester!",
  "wyvern_roles.role.jester.goal": "Act suspicious to get eliminated by a vigilante.",
  "wyvern_roles.role.jester.win": "Jester Wins!",
  
  // a custom win status because the jester is its own team.
  "wyvern_roles.win.other.jester": "They successfully tricked the innocents!"
}
```
