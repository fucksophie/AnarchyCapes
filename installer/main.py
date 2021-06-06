import os

host = "C:\Windows\System32\drivers\etc\hosts"

service = "None"

from appJar import gui

app = gui()

# Check for administrator
if os.name == "nt":
	import ctypes
	try:
		if not ctypes.windll.shell32.IsUserAnAdmin():
			app.infoBox("Noo..", "Start me as administrator!")
			exit()

	except:
		app.infoBox("Noo..", "Start me as administrator!")
		exit()
	
elif os.name == "posix":
	host = "\\etc\\hosts"

	if not os.getuid() == 0:
		app.infoBox("Noo..", "Use sudo to start me.")
		exit()

#Define all needed functions
def line_prepender(filename, line):
    with open(filename, 'r+') as f:
        content = f.read()
        f.seek(0, 0)
        f.write(line.rstrip('\r\n') + '\n' + content)

def remove(file, word):
    dummy_file = file + '.bak'
    is_skipped = False

    with open(file, 'r') as read_obj, open(dummy_file, 'w') as write_obj:
        for line in read_obj:
            if word not in line:
                write_obj.write(line)
            else:
                is_skipped = True

    if is_skipped:
        os.remove(file)
        os.rename(dummy_file, file)
    else:
        os.remove(dummy_file)

def handle(button):

	global service
	
	#Handle services
	if button == "CloaksPlus":
		service = button
		app.setLabel("selected", "Current: " + service)
	if button == "AnarchyCapes":
		service = button
		app.setLabel("selected", "Current: " + service)

	#Handle actions
	if button == "Install":
		remove(host, "s.optifine.net")
		app.setLabel("selected", "Current: " + service)
		app.infoBox("Yay!", "Installed " + service + "!")
		if service == "AnarchyCapes":
			line_prepender(host, "54.37.139.51 s.optifine.net")
		elif service == "CloaksPlus":
			line_prepender(host, "159.203.120.188 s.optifine.net")
	elif button == "Uninstall":
		remove(host, "s.optifine.net")
		app.infoBox("Yay!", "Uninstalled all Services!")
	pass

app.setBg("#2A2D32")
app.setFg("#FFFFFF")
app.setResizable(canResize=False)

app.addLabel("title", "Cape Service Installer")

app.addLabel("actions", "Actions")
app.addButtons(["Uninstall", "Install"], handle)

app.addLabel("services", "Services")
app.addButton("AnarchyCapes", handle)
app.addButton("CloaksPlus", handle)
app.addLabel("selected", "Current: " + service)

app.go()