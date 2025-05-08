# Leechit

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
  - (1) help                  - prints this message
  - (2) login                 - login into your account
  - (3) register              - create an account
  - (4) exit                  - terminate the session

USER commands:
  - (5)  download <torrent>    - download a torrent from registry
  - (6)  upload <torrent>      - upload a torrent to the registry
  - (7)  inspect <torrent>     - inspect what a torrent contains
  - (8)  remove <torrent>      - removes torrent form registry
  - (9)  registry              - print available files in the registry
  - (10) logout                - logs out the user if logged in

## Torrents

The torrents included are example torrents and they are just a simple
way of testing the code, they were handcrafter by me to test different scenarious.
