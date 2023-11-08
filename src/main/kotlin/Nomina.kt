class Nomina(var nombEmp: String = "",
             var apeEmp: String = "",
             var nEmp: Int = 0,
             var salBase: Double = 0.0,
             var hsTrab: Int = 0,
             var deducc: Double = 0.0,
             var fechPag: String = "")
{
    override fun toString(): String {
        return "Modelo.Nomina(nomb_emp='$nombEmp', ape_emp='$apeEmp', n_emp=$nEmp, sal_base=$salBase, hs_trab=$hsTrab, deducc=$deducc, fech_pag='$fechPag')"
    }

}