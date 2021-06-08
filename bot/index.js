const Discord = await import("discord.js");
const fs = await import("fs")
import urlExist from "url-exist"
import request from "request"

const prefix = "!"

const bot = new Discord.Client({
	disableMentions: "everyone"
})  

bot.on("ready", async () => {
	console.log("bot on")

	await bot.user.setActivity({
		name: "VANO 3000 - Running Away",
		type: "LISTENING"
	})
})

bot.on("message", async message => {
	const args = message.content.split(" ")
	
	if(message.author.bot) return;

	let command = args.shift()
	if(!command.startsWith(prefix)) return; else command = command.substring(1);
	
	if(command == "cape") {
		if(args[0]) {
 			const exists = await urlExist("http://54.37.139.51/capes/" + args[0] + ".png");

			if(!exists) {
				message.channel.send(":x: No cape found for " + args[0])
			} else {
				message.channel.send("http://54.37.139.51/capes/" + args[0] + ".png")
			}
		} else {
			message.channel.send(":x: Missing a username.")
		}
	} else if(command == "status"){
		request("https://mcapi.us/server/status?ip=mc.capes.yourfriend.lv", {}, (err, res, body)=>{
			if(err) console.log(err)
			body = JSON.parse(body);

			if(body.status == "error") {
				message.channel.send("Server is down. Experienced error: `" + body.error + "`.")
			} else if(!body.online) {
				message.channel.send("Server is natrually down. DM yourfriend to restart it.")
			} else {
				message.channel.send("Server is up! Online: " + body.players.max + "/" + body.players.now + "!")
			}

		})
	} else if(command == "help") {
		message.channel.send("!status to check server status, and !cape <USERNAME> to view somebody's cape!")
	}
})

bot.login(fs.readFileSync("token").toString())