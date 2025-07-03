select 
	tvtds.ldt_year_month as ldt_year_month,
	tvtds.co_region as co_region,
	tvtds.co_section_road as co_section_road,
	sum(tvtds.nb_anomalies) as count_anomaly,
	sum(tvtds.nb_infractions) as count_infraction,
	sum(tvtds.nb_valid_trips) as count_valid
from t_vehicle_trip_daily_summary tvtds
where tvtds.co_region = ?
and tvtds.ldt_year_month = ?
group by tvtds.ldt_year_month,
		tvtds.co_region,
		tvtds.co_section_road;