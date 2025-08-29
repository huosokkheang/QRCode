package com.solo.controller;

import com.core.collection.Solo;
import com.core.util.operation.ShellScript;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shellScript")
public class ExecuteScriptController {

    @PostMapping("/executeShellFile")
    public Solo executeShellFile(@RequestBody Solo param) {
        Solo solo = new Solo();
        String execute = ShellScript.executeShellFile(param.getString("shellScriptPath"));
        solo.setString("message", execute);
        return solo;
    }

    @PostMapping("/executeBatFile")
    public Solo executeBatFile(@RequestBody Solo param) {
        Solo solo = new Solo();
        String execute = ShellScript.executeBatFile(param.getString("shellScriptPath"));
        solo.setString("message", execute);
        return solo;
    }

    @PostMapping("/executeBatCommand")
    public Solo executeBatCommand(@RequestBody Solo param) throws Exception {
        Solo solo = new Solo();
        String execute = ShellScript.executeBatCommand(param.getString("shellScriptPath"));
        solo.setString("message", execute);
        return solo;
    }

    @PostMapping("/executeShellCommand")
    public Solo executeShellCommand(@RequestBody Solo param) throws Exception {
        Solo solo = new Solo();
        String execute = ShellScript.executeShellCommand(param.getString("shellScriptPath"));
        solo.setString("message", execute);
        return solo;
    }
}
