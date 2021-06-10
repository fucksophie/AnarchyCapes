# Code from https://gist.github.com/bitmingw/69bfee10976a68078562a1f881eed5ab
# Several improvements from me.

from urllib.request import urlopen
from subprocess import check_output
import json, time

def github_sync():
    remote_sha = fetch_remove_sha()
    local_sha = fetch_local_sha()
    if remote_sha != local_sha:
        check_output(["git", "pull", "origin", "main"])
        print("The local repo has been updated")
        return 1
    else:
        print("The local repo is already up-to-date")
        return 0


def fetch_remove_sha():
    resp = urlopen("https://api.github.com/repos/yourfriendoss/AnarchyCapes/branches/main")
    resp_str = str(resp.read(), encoding="utf-8")
    resp_data = json.loads(resp_str)
    remote_sha = resp_data["commit"]["sha"]
    return remote_sha


def fetch_local_sha():
    check_output(["git", "checkout", "main"])
    local_sha = str(check_output(["git", "rev-parse", "HEAD"]), encoding="utf-8")
    return local_sha[:-1]


if __name__ == "__main__":
	while True:
		time.sleep(10)
		github_sync()