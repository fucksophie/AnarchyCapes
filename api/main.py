from sanic import Sanic
from sanic.response import json, file, text, redirect
from pathlib import Path

import urllib.request, requests, os.path

if not os.path.exists("auth.txt"):
    print("Missing auth file")
    exit()

key = open("auth.txt").read()

app = Sanic("AnarchyCapes")

opener = urllib.request.build_opener()
opener.addheaders = [('User-agent', 'Mozilla/5.0')]
urllib.request.install_opener(opener)

if not os.path.exists("capes"):
    os.makedirs("capes")

def optifine_exists(path):
    try:
        with requests.get(f"http://107.182.233.85/capes/{path}", stream=True) as response:
            try:
                response.raise_for_status()
                return True
            except requests.exceptions.HTTPError:
                return False
    except requests.exceptions.ConnectionError:
        return False

@app.post('/v1/update')
async def update(request):
    if "auth" in request.json:
        if request.json["auth"] == key:
            try:
                response = requests.get(request.json["image"])
        
                urllib.request.urlretrieve(request.json["image"], "capes/" + request.json["username"]+".png")
        
                return json({
                    "message": "",
                    "error": False
                })
            except requests.ConnectionError:
                return json({
                    "message": "URL does not exist.",
                    "error": True
                })
        else:
            return json({
                "message": "Server authenication failed.",
                "error": True
            })
    else:
        return json({
            "message": "Missing authenication.",
            "error": True
        })

@app.get("/capes/<path:path>")
async def capes(request, path):
    name = path[:-4]

    print(name + " - Routing cape")
    if os.path.exists("capes/" + path):
        
        print(name + " - AnarchyCapes cape exists, routing.")
        return await file("capes/" + path)
    elif optifine_exists(path):
        print(name + " - Optifine cape exists, routing.")
        return redirect(f"http://107.182.233.85/capes/{path}")
    else:
        print(name + " - Could not find cape.")
        return text("404", status=404)
        
if __name__ == '__main__':
	app.run(port=20012, host="0.0.0.0", access_log=False)