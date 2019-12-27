package com.edzzn.command;

import org.apache.felix.service.command.CommandProcessor;
import com.edzzn.api.ISimulation;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + "=elevator",
        CommandProcessor.COMMAND_FUNCTION + "=s",
        CommandProcessor.COMMAND_FUNCTION + "=g"
    },

    service=SimulationCommand.class
)
public class SimulationCommand {
    private ISimulation simulationSvc;

    @Reference
    public void setGreeting(ISimulation simulationSvc) {
        this.simulationSvc = simulationSvc;
    }

    public void s() {
        System.out.println(simulationSvc.getStatus());
    }
    public void g() {
        System.out.println("Another command");
    }
}
