import discord, os, requests, re

from discord.ext import commands

bot = commands.Bot(command_prefix="?")
key = open("auth.txt").readlines()[0]

checkIPRegex = re.compile(
        r'^(?:http|ftp)s?://' # http:// or https://
        r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+(?:[A-Z]{2,6}\.?|[A-Z0-9-]{2,}\.?)|' #domain...
        r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})' # ...or ip
        r'(?::\d+)?' # optional port
        r'(?:/?|[/?]\S+)$', re.IGNORECASE)

@bot.command()
async def register(ctx, username = None, *, image = None):
	if not username:
		return await ctx.reply("Add your username~")
	if not image:
		return await ctx.reply("Add your image~")

	if not re.match(checkIPRegex, image):
		return await ctx.reply("That image is not valid")

	response = requests.post("http://localhost:20012/v1/update", json= {
		"auth": key,
		"image": image,
		"username": username
	}).json()

	if response["error"]:
		await ctx.reply(embed = discord.Embed(title=");", description="Experienced error: " + response["message"], color=0xff0000))
	else:
		await ctx.reply(embed = discord.Embed(title="(;", color=0x00f43e, description="Cape successfully set for " + username + "!"))

@bot.command()
async def cape(ctx, username = None):
	if not username:
		return await ctx.reply("Add your username~")
	
	await ctx.reply("http://54.37.139.51/capes/" + username + ".png")
bot.run(open("token.txt").readlines()[0])