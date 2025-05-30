# Leechit

Leechit is a registry for torrents. It allows its users to upload and share
their own torrents with the community, but also download from the public
torrent collection.

When you download a `torrent` files from the registry it will create a `downloads`
folder where you will find the files described by the `torrent`.

When you start the program you will be moved in a `REPL` where you can access
all the commands based on your status (logged in / logged out).

## Structure

Objects:

- (1) REPL
- (2) Auth
- (3) User
- (4) Decoder
- (5) Torrent (from Decoder)
- (6) File
- (7) Registry
- (8) Post
- Service

Commands (actions):

REPL commands:
  - (1) help                      - prints this message
  - (2) login                     - login into your account
  - (3) register                  - create an account
  - (4) exit                      - terminate the session

USER commands:
  - (5)  download <id torrent>    - download a torrent from registry
  - (6)  upload <id torrent>      - upload a torrent to the registry
  - (7)  inspect <id torrent>     - inspect what a torrent contains
  - (8)  remove <id torrent>      - removes torrent form registry
  - (9)  registry                 - print available files in the registry
  - (10) logout                   - logs out the user if logged in

## Torrents

The torrents included are example torrents and they are just a simple
way of testing the code, they were handcrafter by me to test different scenarious.
