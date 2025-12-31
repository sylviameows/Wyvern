<h1 align="center">Wyvern</h1>
<div align="center">
  <sub>Built with ❤︎ by <a href="https://sylviameo.ws/">sylviameows</a></sub>
  <p>A modding API for <b>The Last Voyage of the Harpy Express</b> (WATHE)</p>
  
  <img alt="Minecraft v1.21.1" src="https://img.shields.io/badge/Minecraft-v1.21.1-green?style=for-the-badge">
  <img alt="Wathe v1.3.2" src="https://img.shields.io/badge/Wathe-v1.3.2-orange?style=for-the-badge&link=https%3A%2F%2Fmodrinth.com%2Fmod%2Fwathe">

  
</div>

---
## Table of Contents
- [About](#about)
- [Features](#features)
- [Examples](#examples)
- [FAQ](#faq)
- [Usage](#usage)
---
## About
Wyvern has 3 parts, __API__, __Core__, and __Roles__. The __API__ is there to make many modding endeavours with Wathe easier and more extendable for developers to add roles, tasks, shops, and more. __Core__ acts as the mod itself, it injects into TMM and replicates the behaviour with the custom API and provides the registries & methods needed for the API. It also provides QOL features and rendering changes that improve the Wathe experience. Finally, __Roles__ is the custom roles created by us. It's separated from Wyvern itself to make the useful API available without needed our specific curated experience. This section of the project has not yet started development.

This mod is required on both the client and server for it to work.

## Features
- __Wyvern Roles__: are simple & extendable, making custom behaviour easier than ever before!
- __Alignments__: now allows a third "team" not working for the killers or passengers.
- __Custom Tasks__: can be easily registered with a new extendable API.
- __Nicknames__: can be set by pressing `J` or running `wyvern:nickname <player> <name>`.
- __Mood Handlers__: are ways to control and display players moods different from what Wathe does normally.
- __Shop API__: roles can be easily given custom shops with custom items.

## Examples
- [Roles](#roles)
- [Custom Shop](#custom-shop)
- [Default Shop](#default-shop)
- [Tasks](#tasks)

### Roles
Custom roles can be created by extending the `Role` class from Wyvern. These must be registered with WyvernAPI in your ModInitializer to work properly.
```java
public class ExampleRole extends Role {
  public static Identifier IDENTIFIER = Mod.id("example");
  public static int COLOR = 0xFFAAFF

  public ExampleRole() {
    // alignments are INNOCENT (wins with passengers), KILLER (wins with killers), and OTHER (requires a custom WinStatus)
    super(IDENTIFER, Alignment.INNOCENT, COLOR);

    // for killer roles you may want to add:
    settings.setInstinct(Instinct.getKiller());
    settings.setShop(Shop.getDefault());
  }

  @Override
  public void assign(PlayerEntity player) {
    // here is where you can give items to the player.
  }

  @Override
  public RoleOptions defaults(RoleOptions.Builder builder) {
    // this tells Wyvern how many of this role to assign (and odds of it appearing if included).
    return builder.count(1).build();
  }
}
```

```java
// inside your mod initializer:
WyvernAPI.roles().register(new ExampleRole());
```

You may notice the translations are missing, so you can add the following to your translations file for each role. Replacing mod_id with your mods id, and example role with your role's identifier.
```json
{
  "mod_id.roles.example.name": "Example Role",
  "mod_id.roles.example.team": "Example Roles",
  "mod_id.roles.example.goal": "Lorem ipsum dolor sit amet."
}
```

### Custom Shop
Making a custom shop is easy, inside your role constructor you can do something like this.
```java
Shop shop = new Shop();
shop.append(new ItemStack(WatheItems.KNIFE, 1), /* cost */ 100, /* type */ ShopItem.Types.WEAPON);
shop.append(new ItemStack(ModItems.EXAMPLE_WEAPON, 1), /* cost */ 150, /* type */ ShopItem.Types.WEAPON);
shop.append(new ItemStack(ModItems.EXAMPLE_POISON, 1), /* cost */ 75, /* type */ ShopItem.Types.POISON);
shop.append(new ItemStack(ModItems.EXAMPLE_TOOL, 2), /* cost */ 100, /* type */ ShopItem.Types.TOOL);

// when making a custom shop, you *must include the following for coins to work*
shop.setStartingBalance(100);
shop.setTicking(/* increment by */ 5, /* coins every X ticks */ Time.getInTicks(0, 10 /* seconds */));

settings.setShop(shop)
```

### Default Shop
The default shop can be modified if you'd like to add or modify entries within it for all roles that build off this shop.
```java
// use this method to get and modify the shop instance.
WyvernAPI.defaultShop().modify(shop -> {
  // add custom items:
  shop.append(new ItemStack(ModItems.EXAMPLE_TOOL, 2), /* cost */ 100, ShopItem.Types.TOOL);

  // modify existing:
  shop.modify(WatheItems.KNIFE.getDefaultStack(), item -> {
    item.setPrice(75);
  }

  // change coin behaviour:
  shop.setStartingBalance(75);
  shop.setTicking(/* increment by */ 10, /* coins every X ticks */ Time.getInTicks(0, 15 /* seconds */));
});

// Now when a role uses one of these methods they'll recieve the modified shop.
WyvernAPI.defaultShop().use();
Shop.gerDefault();
```

### Tasks
WyvernAPI provides 2 basic task implementations and an interface to make your own. These task implementations being `FulfilledTasks` and `TimedTasks`.
Tasks will use `mod_id.task.identifier` for translations.

```java
// creating a timed task.
public final class SleepTask extends TimedTask {

    public static Identifier IDENTIFIER = Wyvern.id("sleep");

    @Override
    public Identifier id() {
        return IDENTIFIER;
    }

    public SleepTask() {
        this(WyvernConstants.TIMED_TASK_DURATION);
    }

    private SleepTask(int time) {
        super(time);
    }

    @Override
    public void tick(@NotNull PlayerEntity player) {
        if (player.isSleeping()) {
            decrement();
        }
    }

    public static SleepTask fromNbt(NbtCompound nbtCompound) {
        return new SleepTask(nbtCompound.getInt("time"));
    }
    
}
```
```java
// creating a fulfilled task.
public final class EatTask extends FulfilledTask {

    public static Identifier IDENTIFIER = Wyvern.id("eat");

    @Override
    public Identifier id() {
        return IDENTIFIER;
    }

}

// then when food is consumed
MoodHandler handler = ((PlayerMoodComponentAccessor) PlayerMoodComponent.KEY.get(this)).wyvern$getMoodHandler();
Task task = handler.getTask();
if (task instanceof EatTask eatTask) {
  eatTask.fulfill();
}
```

```java
// registering the tasks:
WyvernAPI.tasks().register(SleepTask.IDENTIFIER, SleepTask::fromNbt, SleepTask::new);
WyvernAPI.tasks().register(EatTask.IDENTIFIER, EatTask::new);
```

## FAQ

## Usage
> [!IMPORTANT] 
> This API has not yet been published to the repository.
```kts
repositories {
    maven {
        name = "sylviameows"
        url = uri("https://repo.sylviameo.ws/releases/")
    }
}

dependencies {
    // replace with latest version
    compileOnly("net.sylviameows:wyvern:{version}")
}
```
