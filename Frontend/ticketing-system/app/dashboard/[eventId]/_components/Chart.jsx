"use client"

import { TrendingUp } from "lucide-react"
import { CartesianGrid, Line, LineChart, XAxis } from "recharts"

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart"


const chartConfig = {
  desktop: {
    label: "Desktop",
    color: "hsl(var(--chart-1))",
  },
  mobile: {
    label: "Mobile",
    color: "hsl(var(--chart-2))",
  },
}

export function Chart({ eventname, chartData }) {
  return (
    <Card className="bg-blight">
      <CardHeader className="text-center">
        <CardTitle className="text-white">TICKET ANALYSIS</CardTitle>
        <CardDescription className="text-or">{eventname}</CardDescription>
      </CardHeader>
      <CardContent>
        <ChartContainer config={chartConfig}>
          <LineChart
            accessibilityLayer
            data={chartData}
            margin={{
              left: 12,
              right: 12,
            }}
          >
            <CartesianGrid vertical={false} />

            <ChartTooltip cursor={false} content={<ChartTooltipContent />} />
            <Line
              dataKey="totalTicketsAdded"
              type="monotone"
              stroke="#FF6500"
              strokeWidth={2}
              dot={false}
            />
            <Line
              dataKey="totalTicketsSold"
              type="monotone"
              stroke="#ffffff"
              strokeWidth={2}
              dot={false}
            />
          </LineChart>
        </ChartContainer>
      </CardContent>
      <CardFooter>
        <div className="flex justify-center w-full gap-5">
          <div className="flex gap-4">
            <div className="rounded-full h-4 w-4 bg-or"></div>
            <h3 className="text-or"> Total Tickets Added</h3>
          </div>
          <div className="flex gap-4">
            <div className="rounded-full h-4 w-4 bg-white"></div>
            <h3 className="text-white"> Total Tickets Sold</h3>
          </div>
        </div>
      </CardFooter>
    </Card>
  )
}
