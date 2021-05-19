package de.stroehmi.diving.zhl16c.algorithm.zhl16c
import de.stroehmi.diving.zhl16c.environment.BreathingGas
import kotlin.math.pow
/**
 * Represents a compartment the saturation physics of a tissue by its defined halftime value and the calculated factors A and B.
 * @param helium Halftime value for helium in compartment.
 * @param nitrogen Helium Halftime value for nitrogen in compartment.
 * @param id Id of compartment (e.g. currently 16 compartments from 0..15).
 */
internal class CompartmentHalftime(val helium: Double, val nitrogen: Double, val id: Int) {
    /**
     * Calculates A factor combined factor for all inert gases in current breathing gas.
     * @param breathingGas Current breathing gas.
     * @param gradientFactor Current gradient factor (consider slope manually based on current deco stop).
     * @return Factor A.
     */
    fun a(breathingGas: BreathingGas, gradientFactor: Double) : Double {
        val factorANitrogen = a(InertGasComponent.Nitrogen, gradientFactor)
        val factorAHelium   = a(InertGasComponent.Helium, gradientFactor)
        return ((factorANitrogen * breathingGas.inertGasFactorNitrogen) + (factorAHelium * breathingGas.inertGasFactorHelium)) / (breathingGas.inertGasFactorNitrogen + breathingGas.inertGasFactorHelium)
    }
    /**
     * Calculates B factor combined factor for all inert gases in current breathing gas.
     * @param breathingGas Current breathing gas.
     * @param gradientFactor Current gradient factor (consider slope manually based on current deco stop).
     * @return Factor B.
     */
    fun b(breathingGas: BreathingGas, gradientFactor: Double) : Double {
        val factorBNitrogen = b(InertGasComponent.Nitrogen, gradientFactor)
        val factorBHelium   = b(InertGasComponent.Helium, gradientFactor)
        return ((factorBNitrogen * breathingGas.inertGasFactorNitrogen) + (factorBHelium * breathingGas.inertGasFactorHelium)) / (breathingGas.inertGasFactorNitrogen + breathingGas.inertGasFactorHelium)
    }
    /**
     * Calculates factor A for one of the inert gas components.
     * @param component Helium or Nitrogen.
     * @param gradientFactor Current gradient factor (consider slope manually based on current deco stop).
     * @return Factor A.
     */
    private fun a(component: InertGasComponent, gradientFactor: Double = 1.0) : Double {
        val halftime = if(component == InertGasComponent.Helium) this.helium else this.nitrogen
        val a = 2 * halftime.pow(-1.00/3.00) // must be double devided by double otherwise big trouble :-)
        return a * gradientFactor
    }
    /**
     * Calculates factor B for one of the inert gas components.
     *
     * @param component Helium or Nitrogen.
     * @param gradientFactor Current gradient factor (consider slope manually based on current deco stop).
     * @return Factor B.
     */
    private fun b(component : InertGasComponent, gradientFactor: Double = 1.0) : Double {
        val halftime = if(component == InertGasComponent.Helium) this.helium else this.nitrogen
        val b = 1.005 - halftime.pow(-0.5)
        return (b / (gradientFactor - (gradientFactor * b) + b))
    }

    override fun toString() = "Compartment No. $id\t\tNitrogen: $nitrogen\t\tHelium: $helium"
}