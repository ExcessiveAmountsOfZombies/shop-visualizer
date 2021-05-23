# Shop Visualizer
Server and mod pair to allow the client to render what is in a shop. Each by themselves will do nothing. 

The idea is that instead of adding an extra item that needs to be accounted for on the server as many shop plugins do, it places all the rendering requirements on the client. The reduction in load on the server can be 0-2 entities, depending on how users decide to display items in their shops (Item frames, plugin implmentations that add an item that floats on top etc).

### Client
Will render the item being sold in the shop above the chest.

### Server
Currently supports only [ChestShop](https://github.com/ChestShop-authors/ChestShop-3) out of the box, but the project is WIP and adding more is a possibility. There is also a command system that you can use to apply blocks/item rendering to any block entities (chests, signs, furnaces, beacons etc).



![Chestshop Demonstration](https://i.imgur.com/jvpUnKS.png)
