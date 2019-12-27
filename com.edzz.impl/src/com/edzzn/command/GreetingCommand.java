package com.edzzn.command;

import org.apache.felix.service.command.CommandProcessor;
import com.edzzn.api.Greeting;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + "=example",
        CommandProcessor.COMMAND_FUNCTION + "=g"
    },

    service=GreetingCommand.class
)
public class GreetingCommand {
    private Greeting greetingSvc;

    @Reference
    public void setGreeting(Greeting greetingSvc) {
        this.greetingSvc = greetingSvc;
    }

    public void g() {
        System.out.println(greetingSvc.sayHello());
    }
}
