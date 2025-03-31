# ![jsw](resources/icon128.png) Java Sandbox World

---

## Description

**JSandboxWorld** - is a powerful library for reading, editing, writing and generating buildings for the game
Sandbox World in **.structure** format

---

## Usage

These classes will help you with most of your actions:

**Structure** - Provides convenient reading and writing of structures.
Also allows you to change the version of the structure and get tables of
repeating elements in blocks.

**Root** - Stores the position and rotation of the root, as well as a list of all
blocks that belong to it.

**Block** - Stores information about a specific block. Block type, its
name, connected blocks, metadata and other information.

You can look at the rest yourself in the code, since there is no documentation
at the moment.

---

### Examples of usage

Creating a building with 150 cannons connected to one AND:

```java
Structure structure = new Structure();

Root root = new Root();

List<Block> blocks = root.getBlocks();

Block and = new Block(Blocks.AND);

for(int i = 0;i < 150;i++) {
    Block gun = new Block(Blocks.LIGHT_GUN);
    
    gun.connectInput(and);
    
    blocks.add(gun);
}

blocks.add(and);

structure.getRoots().add(root);

structure.write("guns.structure");
```

---

## Library installation

In the future, the library will be published on Maven Central. In the meantime,
download the **JAR** file from the release that is convenient for you and connect as
conveniently as possible.