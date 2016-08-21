#QINQ Web Connecter Servers.

## Implementation
Both implementations use php and mysql to keep track of/connect games/users.
And the interfaceses are nearly identical

### php-sockets
The faster approach. Connects to the game using a socket. Connects to clients
using WebSockets. Forwards any client messages to game socket, and game
messages to *all* the clients. Clients do not process info not directed to them.

#### Pros
- Faster(much faster)
- Uses less bandwidth
- Stores *almost* nothing in MySQL database

#### Cons
- PHP script must run idefinetly(duration of game)
  - Web Servers(especially free ones) are not happy about this
- PHP must be able to listen on sockets
  - Web Servers(especially free ones) may restrict this feature

### sql-poll
Clients put message into database addressed to game(using code and uid 0,
clients have non-zero uids). Game requests all messages with code and uid 0
from server, and reads them. To get a message from the game to client, the same
possess is used(except the uid is not zero, but rather the one assigned to the
client but the game during the user creation phase).

#### Pros
- More likely to be able to run on WebServer
- PHP scripts run for a shot amount of time
  - So potentially less CPU usage

#### Cons
- Slow(takes a while for a message to get from the game to the client)
- Uses way more bandwidth
- Heavy MySQL reliance
