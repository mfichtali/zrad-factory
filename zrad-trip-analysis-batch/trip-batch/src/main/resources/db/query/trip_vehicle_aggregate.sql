with VehiclePassageCounts as (
select
	vpi.rn_vehicle as rn_vehicle,
	vpi.co_region as co_region,
	vpi.co_section_road as co_section_road,
	vpi.id_event as id_event,
	count(case when vpi.co_radar_detection = 'I' then 1 end) as count_input,
	count(case when vpi.co_radar_detection = 'O' then 1 end) as count_output,
	MIN(case when vpi.co_radar_detection = 'I' then vpi.ldt_passage_date end) as horo_input,
	MAX(case when vpi.co_radar_detection = 'O' then vpi.ldt_passage_date end) as horo_output,
	MAX(case when vpi.co_radar_detection = 'I' then vpi.speed_passage_km end) as speed_input,
	MAX(case when vpi.co_radar_detection = 'O' then vpi.speed_passage_km end) as speed_output
from
	t_vehicle_passage_importer vpi
where
	vpi.co_status_import = 'PENDING'
	and vpi.co_region = ?
	and vpi.co_section_road = ?
	and vpi.id_event = ANY(?)
group by
	vpi.rn_vehicle,
	vpi.co_region,
	vpi.co_section_road,
	vpi.id_event
)
select
	vpc.rn_vehicle as rn_vehicle,
	vpc.co_region as co_region,
	vpc.co_section_road as co_section_road,
	vpc.id_event as id_event,
	vpc.count_input as count_input,
	vpc.count_output as count_output,
	vpc.horo_input as horo_input,
	vpc.horo_output as horo_output,
	vpc.speed_input as speed_input,
	vpc.speed_output as speed_output,
    case
		when vpc.count_input <> 1
		or vpc.count_output <> 1
		or horo_input is null
		or horo_output is null then 1
		else 0
	end as in_anomaly
from
	VehiclePassageCounts vpc;