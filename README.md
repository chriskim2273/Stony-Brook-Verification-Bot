
![Logo](https://preview.redd.it/iofsg3xko3s71.png?width=295&format=png&auto=webp&v=enabled&s=faa24c943ce7fcfcabf29ca261cdcfa5a0b4520f)


# Stony Brook University Verification Discord Bot

With the increasing popularity of the Discord messaging platform for students, I thought it may be neccessary to create a bot that can filter out actual students from those posing as them to improve safety and civility in academic servers. This is a discord bot that I created that can be used to verify if a person attends `Stony Brook University` using their email. I used MongoDB to store the data, Google SMTP to send the pin code, and Discord JDA API for the bot itself.

This can me modified to be utilized for other school emails and I am considering making it flexible for any university or email.

## Authors

- [@chriskim2273](https://github.com/chriskim2273)


## Contributing

Contributions are always welcome! If you see potential in the project, please feel free to reach out to me and contribute! christopherkim2273@gmail.com


## Technologies and APIs Used

**Bot:** Discord Java JDA

**Emailing:** Google SMTP

**Database:** MongoDB




## Roadmap

- Encrypt Student Emails

- Add flexibility for multiple universities.


## Usage/Examples

A user who verifies once doesn't have to verify again for any other server. It will verify them for all the servers they are in and automatically verify them in other servers that have this bot. It will also verify them the moment they join a server that has this bot as well. (by verify I mean assign the roles).

**How does it work?**
First, the bot will send a private message to the user, either when they join the server (if you enable the on join setting), or if they use the command:
`!verify`

The private message will prompt the user for their Stony Brook Email Address.
After they send it, the bot will send their email a randomly generated pin, which they must send back to the bot within 5 minutes.
If they successfully enter the right pin, they are now Verified!

**Setup:**
Once you invite the bot to your server, you should notice a new text channel at the top of your channels list of your server. Do not be alarmed, this is simply a setup channel and will automatically delete itself once you do the mandatory command (or you can delete it yourself of course if it doesn't do it itself).
The command you have to do is:
`!setroles @(role-name)`
You have to mention the role and you can mention multiple.

****(MAKE SURE TO MAKE THE BOT'S ROLE HIGHER/ABOVE THE ROLES YOU WANT IT TO ASSIGN, OTHERWISE, IT WON'T CAUSE IT CAN'T!)****
## Discord Bot Invite

*The bot is currently under re-development... When it is finished, the `readme.md` will be updated with the invite link.*
