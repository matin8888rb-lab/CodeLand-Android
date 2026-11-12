import contextlib, io, json
def run(source):
    output = io.StringIO()
    scope = {"__name__": "__main__"}
    try:
        with contextlib.redirect_stdout(output):
            exec(compile(source, "<codeland>", "exec"), scope, scope)
        return json.dumps({"ok": True, "output": output.getvalue()}, ensure_ascii=False)
    except Exception as exc:
        return json.dumps({"ok": False, "output": output.getvalue(), "error": type(exc).__name__ + ": " + str(exc)}, ensure_ascii=False)
