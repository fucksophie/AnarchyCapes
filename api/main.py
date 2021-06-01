from sanic import Sanic
from sanic.response import json
from pathlib import Path

import urllib.request, requests, os.path

if not os.path.exists("auth.txt"):
    print("Missing auth file")
    exit()

key = open("auth.txt").read()
capeDir = str(Path.home()) + "/website/capes/"

app = Sanic("AnarchyCapes")

opener = urllib.request.build_opener()
opener.addheaders = [('User-agent', 'Mozilla/5.0')]
urllib.request.install_opener(opener)

if not os.path.exists(capeDir):
    os.makedirs(capeDir)

@app.post('/v1/update')
async def test(request):
    if "auth" in request.json:
        if request.json["auth"] == key:
            try:
                response = requests.get(request.json["image"])
        
                urllib.request.urlretrieve(request.json["image"], capeDir + request.json["username"]+".png")
        
                return json({
                    "message": "",
                    "error": False
                })
            except requests.ConnectionError as exception:
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

if __name__ == '__main__':
	app.run(port=20012,host="0.0.0.0")