package com.edzzn.command;

import org.apache.felix.service.command.CommandProcessor;
import com.edzzn.api.ISimulation;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + "=elevator",
        CommandProcessor.COMMAND_FUNCTION + "=s",
        CommandProcessor.COMMAND_FUNCTION + "=r",
        CommandProcessor.COMMAND_FUNCTION + "=d",
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
    	// Get Status
        System.out.println(simulationSvc.getStatus());
    }
    
    public void r(int origin, int destination) {
    	// Register a new request
    	simulationSvc.requestRide(origin, destination);
        System.out.println(simulationSvc.getStatus());
    }
    
    public void d() {
    	// return debug output
        System.out.println(simulationSvc.debug());
    }
}
