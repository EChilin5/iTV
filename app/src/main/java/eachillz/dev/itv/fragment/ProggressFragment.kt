package eachillz.dev.itv.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidplot.xy.*
import eachillz.dev.itv.R
import eachillz.dev.itv.databinding.FragmentHomeBinding
import eachillz.dev.itv.databinding.FragmentProggressBinding
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*


class ProggressFragment : Fragment() {

    private var _binding: FragmentProggressBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProggressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val domainLabels = arrayOf<Number>(1,2,3,4,5,6,7,8,9,19)
        val series1Number = arrayOf<Number>(1,2,3,4,5,6,10,5,7,2)

        val series : XYSeries = SimpleXYSeries( Arrays.asList(*series1Number), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")

        val series1Format = LineAndPointFormatter(Color.BLUE, Color.BLACK, null, null)

        series1Format.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))

        binding.plot.addSeries(series, series1Format)

        binding.plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(object : Format() {
            override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition?): StringBuffer? {
                val i = Math.round((obj as Number).toFloat())
                return toAppendTo.append(domainLabels[i])
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }
        })

        PanZoom.attach(binding.plot)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProggressFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}