-- t_vehicle_trip_anomaly
select 
	tvta.ldt_year_month as ldt_year_month,
    tvta.ldt_day as ldt_day,
	tvta.co_region as co_region,
	tvta.co_section_road as co_section_road,
	tvta.co_issue_type_ref as co_issue,
	count(tvta.id) as count_anomaly,
	0 as count_infraction,
	0 as count_valid
from t_vehicle_trip_anomaly tvta
where tvta.co_region = ?
and tvta.ldt_year_month = ?
group by tvta.ldt_year_month,
        tvta.ldt_day,
		tvta.co_region,
		tvta.co_section_road,
		tvta.co_issue_type_ref
		
UNION all

-- t_vehicle_trip_infraction
select 
    tvti.ldt_year_month as ldt_year_month,
    tvti.ldt_day as ldt_day,
    tvti.co_region as co_region,
    tvti.co_section_road as co_section_road,
    tvti.co_issue_type_ref as co_issue,
	0 as count_anomaly,
	count(tvti.id) as count_infraction,
	0 as count_valid
from t_vehicle_trip_infraction tvti
where tvti.co_region = ?
and tvti.ldt_year_month = ?
group by tvti.ldt_year_month,
		tvti.ldt_day,
		tvti.co_region,
		tvti.co_section_road,
		tvti.co_issue_type_ref

UNION all

-- t_vehicle_trip_valid
select 	
    tvtv.ldt_year_month as ldt_year_month,
    tvtv.ldt_day as ldt_day,
    tvtv.co_region as co_region,
    tvtv.co_section_road as co_section_road,
    'OK' as co_issue,
    0 as count_anomaly,
	0 as count_infraction,
	count(tvtv.id) as count_valid
from t_vehicle_trip_valid tvtv 
where tvtv.co_region = ?
and tvtv.ldt_year_month = ?
group by tvtv.ldt_year_month,
        tvtv.ldt_day,
		tvtv.co_region,
		tvtv.co_section_road;