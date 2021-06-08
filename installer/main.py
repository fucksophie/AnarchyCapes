import os

host = "C:\Windows\System32\drivers\etc\hosts"

from appJar import gui

app = gui("[AC]")

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
	#Handle actions
	if button == "Install":
		remove(host, "s.optifine.net")

		with open(host, 'r+') as f:
			content = f.read()
			f.seek(0, 0)
			f.write("54.37.139.51 s.optifine.net".rstrip('\r\n') + '\n' + content)

		app.infoBox("Yay!", "Installed AnarchyCapes!")
	elif button == "Uninstall":
		remove(host, "s.optifine.net")
		app.infoBox("Yay!", "Uninstalled AnarchyCapes!!")
	pass

app.setBg("#2A2D32")
app.setFg("#FFFFFF")
app.setResizable(canResize=False)

app.addLabel("title", "AnarchyCapes Installer")

app.addButtons(["Uninstall", "Install"], handle)
app.go()