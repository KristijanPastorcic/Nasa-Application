package hr.algebra.nasaapplication.factory

import android.content.Context
import hr.algebra.nasaapplication.dao.NasaSqlHelper

fun getNasaRepository(context: Context?) = NasaSqlHelper(context)
